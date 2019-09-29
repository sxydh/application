import sqlite3
import traceback
from finance.consume_me import *
from finance.account import *
from finance.user import *
import wx.lib.scrolledpanel as scrolled
import os


def query(sql, data=[]):
    conn = sqlite3.connect(get_db_path())
    c = conn.cursor()

    try:
        c.execute(sql, data)
        rows = c.fetchall()
        conn.close()
        print("query(sql=" + sql + ", param=" + str(data) + ") -> " + str(rows))
        return rows
    except Exception:
        wx.MessageBox("query(sql=" + sql + ", param=" + str(data) + ") -> " + str(Exception), 'Info', wx.OK | wx.ICON_INFORMATION)
        return None


def dml(dmls):
    conn = sqlite3.connect(get_db_path())
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
        wx.MessageBox("dml(sql=" + dml[0] + ", param=" + str(dml[1]) + ") -> " + str(Exception), 'Info', wx.OK | wx.ICON_INFORMATION)
    conn.close()
    return result


def layout(obj):
    upper = obj
    while upper is not None:
        print(str(type(upper)) + ".Layout()")
        upper.Layout()
        upper = upper.GetParent()


def get_db_path():
    return os.getcwd() + "\me.db"


class Main(wx.Frame):

    def __init__(self, *args, **kwargs):
        super(Main, self).__init__(*args, **kwargs)
        self.panel = scrolled.ScrolledPanel(self, wx.ID_ANY)
        self.panel.SetupScrolling()
        self.box = wx.BoxSizer(wx.VERTICAL)
        self.panel.SetSizer(self.box)
        self.box.Add(ConsumeMe(self.panel, size=(1000, 600)))

        self.menu_bar = wx.MenuBar()
        self.SetMenuBar(self.menu_bar)
        self.init_menu()

    def init_menu(self):
        menu_table = wx.Menu()
        item_consume_me = wx.MenuItem(menu_table, wx.ID_ANY, "个人消费")
        item_account = wx.MenuItem(menu_table, wx.ID_ANY, "所有账户")
        item_user = wx.MenuItem(menu_table, wx.ID_ANY, "所有用户")
        menu_table.Bind(wx.EVT_MENU, self.to_consume_me, source=item_consume_me)
        menu_table.Bind(wx.EVT_MENU, self.to_account, source=item_account)
        menu_table.Bind(wx.EVT_MENU, self.to_user, source=item_user)
        menu_table.Append(item_consume_me)
        menu_table.AppendSeparator()
        menu_table.Append(item_account)
        menu_table.Append(item_user)
        self.menu_bar.Append(menu_table, title="财务")

    def to_consume_me(self, event=None):
        """
        Layout()
        """
        self.box.Clear(delete_windows=True)
        self.box.Add(ConsumeMe(self.panel, size=(1000, 600)))
        layout(self.panel)

        if event is not None:
            event.Skip()

    def to_account(self, event=None):
        """
        Layout()
        """
        self.box.Clear(delete_windows=True)
        self.box.Add(Account(self.panel, size=(1000, 600)))
        layout(self.panel)

        if event is not None:
            event.Skip()

    def to_user(self, event=None):
        """
        Layout()
        """
        self.box.Clear(delete_windows=True)
        self.box.Add(User(self.panel, size=(1000, 600)))
        layout(self.panel)

        if event is not None:
            event.Skip()

    def layout(self, event=None):
        m.layout(self)
        if event is not None:
            event.Skip()
        return self


def main():
    app = wx.App()
    frame = Main(None, title='Main', size=(1000, 600))
    frame.Center()
    frame.Show()
    # frame.Maximize(True)
    app.MainLoop()


if __name__ == '__main__':
    main()
