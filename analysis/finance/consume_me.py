import wx
import sqlite3

conn = sqlite3.connect('../me.db')
c = conn.cursor()


def select(sql, *args):
    c.execute(sql)
    for row in c:
        print(row)
    conn.close()


def insert(sql):
    c.execute(sql)
    conn.commit()
    conn.close()


class ConsumeMe(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent)

        self.box_up = wx.BoxSizer(wx.VERTICAL)
        self.box_down = wx.BoxSizer(wx.VERTICAL)

        self.box = wx.BoxSizer(wx.VERTICAL)
        self.box.AddMany([self.box_up, self.box_down])
        self.SetSizer(self.box)

        self.init_ui()

    def init_ui(self):
        row = Row(self).load_new()
        self.box_down.Add(row)


class Row(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent)
        self.heignt_row = 50
        self.width_row = 120
        self.readonly = "#919191"

        self.detail_boxes = []
        self.keycodes = []
        self.sqls = []

        self.left_box = wx.BoxSizer(wx.HORIZONTAL)
        self.right_box = wx.BoxSizer(wx.HORIZONTAL)

        self.box = wx.BoxSizer(wx.HORIZONTAL)
        self.box.AddMany([self.left_box, self.right_box])
        self.SetSizer(self.box)

        font = wx.Font(pointSize=20, family=wx.FONTFAMILY_DEFAULT, style=wx.FONTSTYLE_ITALIC,
                       weight=wx.FONTWEIGHT_BOLD,
                       encoding=wx.FONTENCODING_SYSTEM)
        self.date = wx.TextCtrl(self, id=wx.ID_ANY, value="date", size=(self.width_row, self.heignt_row),
                                style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL | wx.TE_NO_VSCROLL)
        self.date.SetFont(font)

        self.week = wx.TextCtrl(self, id=wx.ID_ANY, value="week", size=(self.width_row, self.heignt_row),
                                style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL | wx.TE_READONLY)
        self.week.SetFont(font)
        self.week.SetBackgroundColour(self.readonly)

        self.account = wx.TextCtrl(self, id=wx.ID_ANY, value="account", size=(self.width_row, self.heignt_row),
                                   style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL)
        self.account.SetFont(font)

        self.category = wx.TextCtrl(self, id=wx.ID_ANY, value="category", size=(self.width_row, self.heignt_row),
                                    style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL)
        self.category.SetFont(font)

        self.sum = wx.TextCtrl(self, id=wx.ID_ANY, value="sum", size=(self.width_row, self.heignt_row),
                               style=wx.TE_MULTILINE | wx.TE_NO_VSCROLL | wx.TE_READONLY)
        self.sum.SetFont(font)
        self.sum.SetBackgroundColour(self.readonly)

        self.left_box.AddMany([self.date, self.week, self.account, self.category, self.sum])

    def load_new(self):
        self.add_detail()
        return self

    def add_detail(self):
        detail_box = wx.BoxSizer(wx.VERTICAL)
        self.right_box.Add(detail_box)
        detail_name = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(self.width_row / 2, self.heignt_row / 2),
                                  style=wx.TE_PROCESS_ENTER)
        detail_value = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(self.width_row / 2, self.heignt_row / 2),
                                   style=wx.TE_PROCESS_ENTER)
        detail_box.Add(detail_name)
        detail_box.Add(detail_value)

        detail_name.Bind(wx.EVT_KEY_DOWN, self.key_down_detail)
        detail_value.Bind(wx.EVT_KEY_DOWN, self.key_down_detail)

        self.detail_boxes.append(detail_box)

        self.Layout()
        self.GetParent().Layout()

    def key_down_detail(self, event):
        key_code = event.GetKeyCode()
        print("key_code: " + str(key_code))
        self.keycodes.append(key_code)
        if len(self.keycodes) > 10:
            del self.keycodes[0]

        if self.keycodes[len(self.keycodes) - 1] == wx.WXK_TAB:
            not_empty = 0
            for detail_box in self.detail_boxes:
                for child in detail_box.GetChildren():
                    if child.GetWindow().GetValue() != "":
                        not_empty += 0.5
            if len(self.detail_boxes) == not_empty:
                self.add_detail()

        elif self.keycodes[len(self.keycodes) - 1] == wx.WXK_DELETE:
            detail_box = self.get_detail_sizer(event.GetEventObject())
            if len(self.detail_boxes) == 1:
                return
            self.right_box.Hide(detail_box)
            self.right_box.Remove(detail_box)
            self.detail_boxes.remove(detail_box)
            self.right_box.Layout()

        elif self.keycodes[len(self.keycodes) - 2] == 308 and self.keycodes[len(self.keycodes) - 1] == 83:
            self.save_row()

        event.Skip()

    def get_detail_sizer(self, widget):
        for detail_box in self.detail_boxes:
            for child in detail_box.GetChildren():
                if widget.GetId() == child.GetWindow().GetId():
                    return detail_box

    def save_row(self):
        children = self.left_box.GetChildren()
        account_name = children[0].GetWindow().GetValue()
        category_name = children[1].GetWindow().GetValue()
        user_name = children[2].GetWindow().GetValue()
        for detail_box in self.detail_boxes:
            detail = detail_box.GetChildren()
            name = detail[0].GetWindow().GetValue()
            value = detail[1].GetWindow().GetValue()
            account_id = select(" SELECT id FROM account WHERE name = ? ")
            print(name + "\r\n" + value)


def main():
    app = wx.App()
    frame = wx.Frame(None, title='Main', size=(800, 600))
    ConsumeMe(parent=frame)
    frame.Center()
    frame.Show()
    app.MainLoop()


if __name__ == '__main__':
    main()
