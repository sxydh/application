from finance.consume_me import *

import os

db_path = os.path.abspath("me.db")


class Main(wx.Frame):

    def __init__(self, *args, **kwargs):
        super(Main, self).__init__(*args, **kwargs)
        self.panel = wx.Panel(self)
        self.box = wx.BoxSizer(wx.VERTICAL)
        self.panel.SetSizer(self.box)
        self.box.AddMany([ConsumeMe(self.panel)])


def main():
    app = wx.App()
    frame = Main(None, title='Main', size=(800, 600))
    frame.Show()
    app.MainLoop()


if __name__ == '__main__':
    main()
