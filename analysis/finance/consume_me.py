import wx
import sqlite3
import re
import datetime
import traceback


def select(sql, param):
    conn = sqlite3.connect('../me.db')
    c = conn.cursor()

    c.execute(sql, param)
    rows = c.fetchall()
    conn.close()
    print("select(sql=" + sql + ", param=" + str(param) + ") -> " + str(rows))
    return rows


def insert(list):
    conn = sqlite3.connect('../me.db')
    c = conn.cursor()

    try:
        for ele in list:
            print(ele)
            result = c.execute(ele[0], ele[1])
            print("insert(sql=" + ele[0] + ", param=" + str(ele[1]) + ") -> " + str(result))
        conn.commit()
    except Exception:
        print(Exception)
        conn.rollback()
        traceback.print_exc()
    conn.close()


class ConsumeMe(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent)

        self.box = wx.BoxSizer(wx.VERTICAL)
        self.box.AddMany([Header(self), List(self), Live(self)])
        self.SetSizer(self.box)


class Header(wx.Panel):

    def __init__(self, parent):
        self.height_cell = 25
        self.width_cell = 120

        wx.Panel.__init__(self, parent)
        self.box = wx.BoxSizer(wx.HORIZONTAL)
        self.SetSizer(self.box)

        self.date = wx.StaticText(self, label="Date", size=(self.width_cell, self.height_cell),
                                  style=wx.ALIGN_CENTRE_HORIZONTAL)
        self.week = wx.StaticText(self, label="Week", size=(self.width_cell, self.height_cell),
                                  style=wx.ALIGN_CENTRE_HORIZONTAL)
        self.account = wx.StaticText(self, label="Account", size=(self.width_cell, self.height_cell),
                                     style=wx.ALIGN_CENTRE_HORIZONTAL)
        self.category = wx.StaticText(self, label="Category",
                                      size=(self.width_cell, self.height_cell),
                                      style=wx.ALIGN_CENTRE_HORIZONTAL)
        self.box.AddMany([self.date, self.week, self.account, self.category])


class List(wx.Panel):
    def __init__(self, parent):
        wx.Panel.__init__(self, parent)
        self.update_data(event=None)

    def update_data(self, event):
        sql = " SELECT GROUP_CONCAT(c.id), c.date, MAX(a.name) account_name, MAX(ct.name) category_name, SUM(c.value), GROUP_CONCAT(c.name) names, GROUP_CONCAT(c.value) \"values\" FROM consume c "
        sql += " INNER JOIN account a ON a.id = c.account_id "
        sql += " INNER JOIN category ct ON ct.id = c.category_id "
        sql += " GROUP BY c.date "
        rows = select(sql=sql, param=())
        print(rows)

        if event is not None:
            event.Skip()


class Live(wx.Panel):
    def __init__(self, parent):
        wx.Panel.__init__(self, parent)

        self.box = wx.BoxSizer(wx.VERTICAL)
        self.box.Add(Row(self).load_new())
        self.SetSizer(self.box)

    def reset(self, event):
        children = self.box.GetChildren()
        print(self.box.GetChildren())
        i = 0
        for child in children:
            self.box.Hide(child.GetWindow())
            self.box.Remove(i)
            i += 1
        print(self.box.GetChildren())
        self.box.Add(Row(self).load_new())
        self.box.Layout()
        print(self.box.GetChildren())

        if event is not None:
            event.Skip()


class Row(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent)
        self.height_cell = 50
        self.width_cell = 120
        self.readonly_color = "#919191"

        self.detail_boxes = []
        self.key_codes = [-100, -100, -100, -100, -100, -100, -100, -100, -100, -100]
        self.sqls = []

        self.left_box = wx.BoxSizer(wx.HORIZONTAL)
        self.right_box = wx.BoxSizer(wx.HORIZONTAL)

        self.box = wx.BoxSizer(wx.HORIZONTAL)
        self.box.AddMany([self.left_box, self.right_box])
        self.SetSizer(self.box)

        font = wx.Font(pointSize=10, family=wx.FONTFAMILY_DEFAULT, style=wx.FONTSTYLE_NORMAL,
                       weight=wx.FONTWEIGHT_BOLD,
                       encoding=wx.FONTENCODING_SYSTEM)
        self.date = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(self.width_cell, self.height_cell),
                                style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL)
        self.date.Bind(wx.EVT_KILL_FOCUS, self.check_date)
        self.date.Bind(wx.EVT_KEY_DOWN, self.date_shortcut)
        self.date.SetFont(font)

        self.week = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(self.width_cell, self.height_cell),
                                style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL | wx.TE_READONLY)
        self.week.SetFont(font)
        self.week.SetBackgroundColour(self.readonly_color)

        self.account = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(self.width_cell, self.height_cell),
                                   style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL)
        self.account.Bind(wx.EVT_KILL_FOCUS, self.check_account)
        self.account.Bind(wx.EVT_KEY_DOWN, self.account_shortcut)
        self.id_account = self.account.GetId()
        self.account_id = -1
        self.account.SetFont(font)

        self.category = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(self.width_cell, self.height_cell),
                                    style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL)
        self.category.Bind(wx.EVT_KILL_FOCUS, self.check_category)
        self.category.Bind(wx.EVT_KEY_DOWN, self.category_shortcut)
        self.id_category = self.category.GetId()
        self.category_id = -1
        self.category.SetFont(font)

        rows = select(" SELECT id FROM user WHERE name = ? ", ("me",))
        if len(rows) > 0:
            self.user_id = rows[0][0]
        else:
            wx.MessageBox("SELECT id FROM user WHERE name = 'me' -> " + str(rows), "Info",
                          wx.OK | wx.ICON_INFORMATION)
            self.GetTopLevelParent().Close()

        self.sum = wx.TextCtrl(self, id=wx.ID_ANY, value="0.00", size=(self.width_cell, self.height_cell),
                               style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL | wx.TE_READONLY)
        self.sum.SetFont(font)
        self.sum.SetBackgroundColour(self.readonly_color)

        self.left_box.AddMany([self.date, self.week, self.account, self.category, self.sum])

    def category_shortcut(self, event):
        key_code = event.GetKeyCode()
        print("key_code: " + str(key_code))
        text_ctrl = event.GetEventObject()

        self.key_codes.append(key_code)
        if len(self.key_codes) > 10:
            del self.key_codes[0]

        if self.key_codes[len(self.key_codes) - 2] == 308 and self.key_codes[len(self.key_codes) - 1] == 65:
            text_ctrl.SetSelection(0, len(text_ctrl.GetValue()))

        if event is not None:
            event.Skip()

    def account_shortcut(self, event):
        key_code = event.GetKeyCode()
        print("key_code: " + str(key_code))
        text_ctrl = event.GetEventObject()

        self.key_codes.append(key_code)
        if len(self.key_codes) > 10:
            del self.key_codes[0]

        if self.key_codes[len(self.key_codes) - 2] == 308 and self.key_codes[len(self.key_codes) - 1] == 65:
            text_ctrl.SetSelection(0, len(text_ctrl.GetValue()))

        if event is not None:
            event.Skip()

    def date_shortcut(self, event):
        key_code = event.GetKeyCode()
        print("key_code: " + str(key_code))
        text_ctrl = event.GetEventObject()

        self.key_codes.append(key_code)
        if len(self.key_codes) > 10:
            del self.key_codes[0]

        if self.key_codes[len(self.key_codes) - 2] == 308 and self.key_codes[len(self.key_codes) - 1] == 59:
            text_ctrl.SetValue(datetime.datetime.today().strftime('%Y-%m-%d'))
        elif self.key_codes[len(self.key_codes) - 2] == 308 and self.key_codes[len(self.key_codes) - 1] == 65:
            text_ctrl.SetSelection(0, len(text_ctrl.GetValue()))

        if event is not None:
            event.Skip()

    def week_cal(self, event):
        if self.date.GetValue() is not None:
            split = self.date.GetValue().split("-")
            if len(split) == 3:
                week_num = datetime.date(int(split[0]), int(split[1]), int(split[2])).isocalendar()[2]
                if week_num == 1:
                    week_num = "一"
                elif week_num == 2:
                    week_num = "二"
                elif week_num == 3:
                    week_num = "三"
                elif week_num == 4:
                    week_num = "四"
                elif week_num == 5:
                    week_num = "五"
                elif week_num == 6:
                    week_num = "六"
                elif week_num == 7:
                    week_num = "日"
                self.week.SetValue("星期" + week_num)
            else:
                self.week.SetValue("")

        if event is not None:
            event.Skip()

    def check_date(self, event):
        if re.fullmatch(pattern="^\d{4}(\-)\d{2}(\-)\d{2}$", string=self.date.GetValue()) is None:
            self.date.SetForegroundColour(wx.RED)
        else:
            self.date.SetForegroundColour(wx.BLACK)

        self.week_cal(event=None)

        if event is not None:
            event.Skip()

    def check_account(self, event):
        rows = select(" SELECT id FROM account WHERE name = ? ", (event.GetEventObject().GetValue(),))
        if len(rows) == 0:
            self.account.SetForegroundColour(wx.RED)
        else:
            self.account.SetForegroundColour(wx.BLACK)
            self.account_id = rows[0][0]

        if event is not None:
            event.Skip()

    def check_category(self, event):
        rows = select(" SELECT id FROM category WHERE name = ? ", (event.GetEventObject().GetValue(),))
        if len(rows) == 0:
            self.category.SetForegroundColour(wx.RED)
        else:
            self.category.SetForegroundColour(wx.BLACK)
            self.category_id = rows[0][0]

        if event is not None:
            event.Skip()

    def load_new(self):
        self.add_detail()
        return self

    def add_detail(self):
        detail_box = wx.BoxSizer(wx.VERTICAL)
        self.right_box.Add(detail_box)

        detail_name = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(self.width_cell / 2, self.height_cell / 2),
                                  style=wx.TE_PROCESS_ENTER)
        detail_value = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(self.width_cell / 2, self.height_cell / 2),
                                   style=wx.TE_PROCESS_ENTER)
        detail_box.Add(detail_name)
        detail_box.Add(detail_value)

        detail_name.Bind(wx.EVT_KEY_DOWN, self.detail_shortcut)
        detail_value.Bind(wx.EVT_KEY_DOWN, self.detail_shortcut)
        detail_value.Bind(wx.EVT_KILL_FOCUS, self.process_detail_val)

        self.detail_boxes.append(detail_box)

        self.Layout()
        self.GetParent().Layout()

    def process_detail_val(self, event):
        sum_val = 0
        for child in self.detail_boxes:
            detail_value = child.GetChildren()[1].GetWindow()
            value = detail_value.GetValue()
            if value is not None and value != "":
                if re.fullmatch(pattern="(^\d*\.\d+$)|((^\d+$))", string=value) is None:
                    detail_value.SetForegroundColour(wx.RED)
                else:
                    detail_value.SetForegroundColour(wx.BLACK)
                    detail_value.SetValue("{:0.2f}".format(round(float(value), 2)))
                    sum_val += int(float(detail_value.GetValue()) * 100)
        self.sum.SetValue("{:0.2f}".format(sum_val / 100))

        if event is not None:
            event.Skip()

    def detail_shortcut(self, event):
        key_code = event.GetKeyCode()
        print("key_code: " + str(key_code))
        self.key_codes.append(key_code)
        if len(self.key_codes) > 10:
            del self.key_codes[0]

        if self.key_codes[len(self.key_codes) - 1] == wx.WXK_TAB:
            not_empty = 0
            for detail_box in self.detail_boxes:
                childs = detail_box.GetChildren()
                if childs[0].GetWindow().GetValue() != "":
                    not_empty += 0.5
                if re.fullmatch(pattern="(^\d*\.\d+$)|((^\d+$))", string=childs[1].GetWindow().GetValue()) is not None:
                    not_empty += 0.5
            if len(self.detail_boxes) == not_empty:
                self.add_detail()

        elif self.key_codes[len(self.key_codes) - 1] == wx.WXK_DELETE:
            detail_box = self.get_detail_sizer(event.GetEventObject())
            if len(self.detail_boxes) == 1:
                return
            self.right_box.Hide(detail_box)
            self.right_box.Remove(detail_box)
            self.detail_boxes.remove(detail_box)
            self.process_detail_val(event=None)
            self.right_box.Layout()

        elif self.key_codes[len(self.key_codes) - 3] == 308 and self.key_codes[len(self.key_codes) - 2] == 306 and self.key_codes[len(self.key_codes) - 1] == 83:
            self.save()

        if event is not None:
            event.Skip()

    def get_detail_sizer(self, widget):
        for detail_box in self.detail_boxes:
            for child in detail_box.GetChildren():
                if widget.GetId() == child.GetWindow().GetId():
                    return detail_box

    def save(self):
        inserts = []

        for detail_box in self.detail_boxes:
            detail = detail_box.GetChildren()
            detail_name = detail[0].GetWindow().GetValue()
            detail_value = detail[1].GetWindow().GetValue()
            sql = " INSERT INTO consume (date, name, value, ctime, utime, account_id, category_id, user_id) "
            sql += " VALUES (?, ?, ?, DATETIME(CURRENT_TIMESTAMP,'localtime'), DATETIME(CURRENT_TIMESTAMP,'localtime'), ?, ?, ?) "
            param = (self.date.GetValue(), detail_name, detail_value, self.account_id, self.category_id, self.user_id)
            for val in param:
                if val is None or str(val) == "":
                    wx.MessageBox("there is null value in param", "Info",
                                  wx.OK | wx.ICON_INFORMATION)
                    return
            inserts.append((sql, param))

        insert(inserts)
        self.GetParent().reset(event=None)


def main():
    app = wx.App()
    frame = wx.Frame(None, title='Main', size=(800, 600))
    ConsumeMe(parent=frame)
    frame.Center()
    frame.Show()
    app.MainLoop()


if __name__ == '__main__':
    main()
