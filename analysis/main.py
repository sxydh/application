import sqlite3
import traceback
from finance.consume_me import *

import os


def query(sql, data=""):
    conn = sqlite3.connect(get_db_path())
    c = conn.cursor()

    c.execute(sql, data)
    rows = c.fetchall()
    conn.close()
    print("query(sql=" + sql + ", param=" + str(data) + ") -> " + str(rows))
    return rows


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
    conn.close()
    return result


def layout(obj):
    obj.Layout()
    upper = obj.GetParent()
    while upper is not None:
        upper.Layout()
        upper = upper.GetParent()


def get_db_path():
    return os.getcwd() + "\me.db"


class Main(wx.Frame):

    def __init__(self, *args, **kwargs):
        super(Main, self).__init__(*args, **kwargs)
        self.panel = wx.Panel(self)
        box = wx.BoxSizer(wx.VERTICAL)
        self.panel.SetSizer(box)
        box.AddMany([ConsumeMe(self.panel, size=(1000, 600))])


def main():
    app = wx.App()
    frame = Main(None, title='Main', size=(1000, 600))
    frame.Center()
    frame.Show()
    # frame.Maximize(True)
    app.MainLoop()


if __name__ == '__main__':
    main()
