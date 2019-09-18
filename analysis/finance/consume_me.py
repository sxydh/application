import wx


class Row(wx.Panel):

    def __init__(self, *args, **kw):
        super(Row, self).__init__(*args, **kw)
        self.init_ui()

    def init_ui(self):
        self.SetBackgroundColour("#ffd700")
        self.box = wx.BoxSizer(wx.HORIZONTAL)

        self.left_box = wx.BoxSizer(wx.HORIZONTAL)
        self.date = wx.TextCtrl(self, id=wx.ID_ANY, value="2019-09-17 22:17:53", size=(120, 50))
        self.week = wx.TextCtrl(self, id=wx.ID_ANY, value="星期一", size=(120, 50))
        self.account = wx.TextCtrl(self, id=wx.ID_ANY, value="蚂蚁花呗", size=(120, 50))
        self.category = wx.TextCtrl(self, id=wx.ID_ANY, value="饮食-食品", size=(120, 50))
        self.sum = wx.TextCtrl(self, id=wx.ID_ANY, value="20.00", size=(120, 50))
        self.left_box.Add(self.date)
        self.left_box.Add(self.week)
        self.left_box.Add(self.account)
        self.left_box.Add(self.category)
        self.left_box.Add(self.sum)

        self.details = []

        self.right_box = wx.BoxSizer(wx.HORIZONTAL)
        self.button_add_detail = wx.Button(self, wx.ID_ANY, label="insert")
        self.right_box.Add(self.button_add_detail)
        self.Bind(wx.EVT_BUTTON, self.add_detail, self.button_add_detail)

        self.box.Add(self.left_box)
        self.box.Add(self.right_box)
        self.SetSizer(self.box)

    def add_detail(self, event):
        detail_box = wx.BoxSizer(wx.VERTICAL)
        self.right_box.Add(detail_box)
        detail_name = wx.TextCtrl(self, id=wx.ID_ANY, value="生菜", size=(60, 23), style=wx.TE_PROCESS_ENTER)
        detail_value = wx.TextCtrl(self, id=wx.ID_ANY, value="20.00", size=(60, 23), style=wx.TE_PROCESS_ENTER)
        detail_box.Add(detail_name)
        detail_box.Add(detail_value)

        detail_name.Bind(wx.EVT_KEY_DOWN, self.key_down_detail)
        detail_value.Bind(wx.EVT_KEY_DOWN, self.key_down_detail)

        self.details.append((detail_name, detail_value))

        print(self.details)

        self.Layout()

    def key_down_detail(self, event):
        keycode = event.GetKeyCode()
        if keycode == wx.WXK_TAB:
            valids = 0
            for detail in self.details:
                if detail[0].GetValue() != "" and detail[1].GetValue() != "":
                    valids = valids + 1
            print(len(self.details) == valids)
            if len(self.details) == valids:
                self.add_detail(event)
        elif keycode == wx.WXK_DELETE:
            print(event.GetEventObject)
        event.Skip()


def main():
    app = wx.App()
    frame = wx.Frame(None, title='Main', size=(800, 600))
    Row(frame)
    frame.Center()
    frame.Show()
    app.MainLoop()


if __name__ == '__main__':
    main()
