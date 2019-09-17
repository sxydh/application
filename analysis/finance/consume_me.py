import wx


class Row(wx.Panel):

    def __init__(self, *args, **kw):
        super(Row, self).__init__(*args, **kw)
        self.init_ui()

    def init_ui(self):
        self.SetBackgroundColour("#ffd700")
        self.box = wx.BoxSizer(wx.HORIZONTAL)

        self.hbox = wx.BoxSizer(wx.HORIZONTAL)

        self.date = wx.TextCtrl(self, id=wx.ID_ANY, value="2019-09-17 22:17:53", size=(120, 50))
        self.week = wx.TextCtrl(self, id=wx.ID_ANY, value="星期一", size=(120, 50))
        self.account = wx.TextCtrl(self, id=wx.ID_ANY, value="蚂蚁花呗", size=(120, 50))
        self.category = wx.TextCtrl(self, id=wx.ID_ANY, value="饮食-食品", size=(120, 50))
        self.sum = wx.TextCtrl(self, id=wx.ID_ANY, value="20.00", size=(120, 50))

        self.hbox.Add(self.date)
        self.hbox.Add(self.week)
        self.hbox.Add(self.account)
        self.hbox.Add(self.category)
        self.hbox.Add(self.sum)

        self.dbox = wx.BoxSizer(wx.)

        self.box.Add(self.hbox)
        self.SetSizer(self.box)


def main():
    app = wx.App()
    frame = wx.Frame(None, title='Main', size=(800, 600))
    Row(frame)
    frame.Center()
    frame.Show()
    app.MainLoop()


if __name__ == '__main__':
    main()
