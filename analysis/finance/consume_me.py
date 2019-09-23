import wx
import sqlite3
import re
import datetime
import traceback


def query(sql, data=""):
    conn = sqlite3.connect('../me.db')
    c = conn.cursor()

    c.execute(sql, data)
    rows = c.fetchall()
    conn.close()
    print("query(sql=" + sql + ", param=" + str(data) + ") -> " + str(rows))
    return rows


def dml(dmls):
    conn = sqlite3.connect('../me.db')
    c = conn.cursor()

    result = None
    try:
        for dml in dmls:
            c.execute(dml[0], dml[1])
            print("dml(sql=" + dml[0] + ", param=" + str(dml[1]) + ") -> " + str(c.fetchall()))
        conn.commit()
        result = c
    except Exception:
        conn.rollback()
        traceback.print_exc()
    conn.close()
    return result


def layout(obj):
    obj.Layout()
    upper = obj.GetParent()
    while upper is not None:
        upper.Layout()
        upper = upper.GetParent()


class ConsumeMe(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent)

        self.box = wx.BoxSizer(wx.VERTICAL)
        header = Header(self)
        list = List(self)
        live = Live(self)
        live.parent_list = list
        self.box.AddMany([header, list, live])
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
        self.box = wx.BoxSizer(wx.VERTICAL)
        self.SetSizer(self.box)

        self.update_data()

    def update_data(self, event=None):
        while len(self.box.GetChildren()) > 0:
            self.box.Hide(self.box.GetChildren()[0].GetWindow())
            self.box.Remove(0)
        sql = " SELECT GROUP_CONCAT(c.id), c.date, a.name account, ct.name category, PRINTF(\"%.2f\", SUM(c.value) / 100.0) sum, GROUP_CONCAT(c.name) names, GROUP_CONCAT(PRINTF(\"%.2f\", c.value / 100.0)) \"values\" FROM consume c "
        sql += " INNER JOIN account a ON a.id = c.account_id "
        sql += " INNER JOIN category ct ON ct.id = c.category_id "
        sql += " GROUP BY c.date, a.id, ct.id "
        rs = query(sql=sql)
        for r in rs:
            ids = r[0].split(",")
            date = r[1]
            account = r[2]
            category = r[3]
            sum = r[4]
            names = r[5].split(",")
            values = r[6].split(",")
            details = []
            for i in range(0, len(ids)):
                details.append((ids[i], names[i], values[i]))
            self.box.Add(Row(self).load_data(date=date, account=account, category=category, sum=sum, details=details))
        layout(self)

        if event is not None:
            event.Skip()


class Live(wx.Panel):
    def __init__(self, parent):
        wx.Panel.__init__(self, parent)

        self.box = wx.BoxSizer(wx.VERTICAL)
        self.box.Add(Row(self).load_new())
        self.SetSizer(self.box)

    def reset(self, event=None):
        children = self.box.GetChildren()
        i = 0
        for child in children:
            self.box.Hide(child.GetWindow())
            self.box.Remove(i)
            i += 1
        self.box.Add(Row(self).load_new())
        layout(self)

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

        rows = query(" SELECT id FROM user WHERE name = ? ", ("me",))
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

    def category_shortcut(self, event=None):
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

    def account_shortcut(self, event=None):
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

    def date_shortcut(self, event=None):
        key_code = event.GetKeyCode()
        print("key_code: " + str(key_code))
        text_ctrl = event.GetEventObject()

        self.key_codes.append(key_code)
        if len(self.key_codes) > 10:
            del self.key_codes[0]

        if self.key_codes[len(self.key_codes) - 2] == 308 and self.key_codes[len(self.key_codes) - 1] == 59:
            text_ctrl.SetValue(datetime.datetime.today().strftime('%Y-%m-%d'))
            text_ctrl.SetInsertionPointEnd()
        elif self.key_codes[len(self.key_codes) - 2] == 308 and self.key_codes[len(self.key_codes) - 1] == 65:
            text_ctrl.SetSelection(0, len(text_ctrl.GetValue()))
        elif self.key_codes[len(self.key_codes) - 2] == 308 and self.key_codes[len(self.key_codes) - 1] == 13:
            children = self.GetParent().box.GetChildren()
            for i in range(0, len(children)):
                if children[i].GetWindow().GetId() == self.GetId():
                    self.GetParent().box.Insert(index=i, window=Row(self.GetParent()).load_new())
                    layout(self)
                    break
        elif self.key_codes[len(self.key_codes) - 1] == wx.WXK_DELETE:
            msg = wx.MessageBox("sure to delete?", "Info", wx.OK | wx.CANCEL | wx.ICON_INFORMATION)
            if msg == 4:
                dmls = []
                for detail_box in self.detail_boxes:
                    dmls.append((" DELETE FROM consume WHERE id = ? ", (detail_box.id,)))
                result = dml(dmls)
                if result is not None:
                    self.GetParent().box.Hide(self)
                    children = self.GetParent().box.GetChildren()
                    for i in range(0, len(children)):
                        if children[i].GetWindow().GetId() == self.GetId():
                            self.GetParent().box.Remove(i)
                            break
                    layout(self)
        if event is not None:
            event.Skip()

    def week_cal(self, event=None):
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

    def check_date(self, event=None):
        if re.fullmatch(pattern="^\d{4}(\-)\d{2}(\-)\d{2}$", string=self.date.GetValue()) is None:
            self.date.SetForegroundColour(wx.RED)
        else:
            self.date.SetForegroundColour(wx.BLACK)

        self.week_cal()

        if event is not None:
            event.Skip()

    def check_account(self, event=None):
        rows = query(" SELECT id FROM account WHERE name = ? ", (event.GetEventObject().GetValue(),))
        if len(rows) == 0:
            self.account.SetForegroundColour(wx.RED)
        else:
            self.account.SetForegroundColour(wx.BLACK)
            self.account_id = rows[0][0]

        if event is not None:
            event.Skip()

    def check_category(self, event=None):
        rows = query(" SELECT id FROM category WHERE name = ? ", (event.GetEventObject().GetValue(),))
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

    def load_data(self, date, account, category, sum, details):
        self.date.SetValue(date)
        self.week_cal()
        self.account.SetValue(account)
        self.category.SetValue(category)
        self.sum.SetValue(str(sum))
        for detail in details:
            self.add_detail(id=detail[0], name=detail[1], value=detail[2])
        return self

    def add_detail(self, id=None, name="", value=""):
        detail_box = wx.BoxSizer(wx.VERTICAL)
        detail_box.id = id
        self.right_box.Add(detail_box)

        detail_name = wx.TextCtrl(self, id=wx.ID_ANY, value=name, size=(self.width_cell / 2, self.height_cell / 2),
                                  style=wx.TE_PROCESS_ENTER)
        detail_value = wx.TextCtrl(self, id=wx.ID_ANY, value=value, size=(self.width_cell / 2, self.height_cell / 2),
                                   style=wx.TE_PROCESS_ENTER)
        detail_box.AddMany([detail_name, detail_value])

        detail_name.Bind(wx.EVT_KEY_DOWN, self.detail_shortcut)
        detail_value.Bind(wx.EVT_KEY_DOWN, self.detail_shortcut)
        detail_value.Bind(wx.EVT_KILL_FOCUS, self.process_detail_val)

        self.detail_boxes.append(detail_box)

        layout(self)

    def process_detail_val(self, event=None):
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

    def detail_shortcut(self, event=None):
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
            if detail_box.id is not None:
                dmls = [(" DELETE FROM consume WHERE id = ? ", (detail_box.id,))]
                result = dml(dmls)
                if result is None:
                    return
            self.detail_boxes.remove(detail_box)
            self.right_box.Hide(detail_box)
            self.right_box.Remove(detail_box)
            self.process_detail_val()
            layout(self)

        elif self.key_codes[len(self.key_codes) - 3] == 308 and self.key_codes[len(self.key_codes) - 2] == 306 and self.key_codes[len(self.key_codes) - 1] == 83:
            self.save_or_update()

        if event is not None:
            event.Skip()

    def get_detail_sizer(self, widget):
        for detail_box in self.detail_boxes:
            for child in detail_box.GetChildren():
                if widget.GetId() == child.GetWindow().GetId():
                    return detail_box

    def save_or_update(self):
        dmls = []

        for detail_box in self.detail_boxes:
            detail = detail_box.GetChildren()
            detail_id = detail_box.id
            detail_name = detail[0].GetWindow().GetValue()
            detail_value = float(detail[1].GetWindow().GetValue()) * 100
            if detail_id is None:
                param = (self.date.GetValue(), detail_name, detail_value, self.account_id, self.category_id, self.user_id)
                sql = " INSERT INTO consume (date, name, value, ctime, utime, account_id, category_id, user_id) "
                sql += " VALUES (?, ?, ?, DATETIME(CURRENT_TIMESTAMP,'localtime'), DATETIME(CURRENT_TIMESTAMP,'localtime'), ?, ?, ?) "
            else:
                param = (self.date.GetValue(), detail_name, detail_value, self.account_id, self.category_id, self.user_id, detail_id)
                sql = " UPDATE consume SET id = id "
                sql += ", date = ? "
                sql += ", name = ? "
                sql += ", value = ? "
                sql += ", utime = DATETIME(CURRENT_TIMESTAMP,'localtime') "
                sql += ", account_id = ? "
                sql += ", category_id = ? "
                sql += ", user_id = ? "
                sql += " WHERE id = ? "
            for val in param:
                if val is None or str(val) == "":
                    wx.MessageBox("there is null value in param", "Info",
                                  wx.OK | wx.ICON_INFORMATION)
                    return
            dmls.append((sql, param))

        if len(dmls) > 0:
            result = dml(dmls)
            if result is not None:
                self.detail_boxes.clear()
                if detail_id is None:
                    self.GetParent().reset()
                    self.GetParent().parent_list.update_data()
                else:
                    self.GetParent().update_data()


def main():
    app = wx.App()
    frame = wx.Frame(None, title='Main', size=(800, 600))
    ConsumeMe(parent=frame)
    frame.Center()
    frame.Show()
    app.MainLoop()


if __name__ == '__main__':
    main()
