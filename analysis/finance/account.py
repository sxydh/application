import wx
import re
import datetime
import main as m
import wx.lib.scrolledpanel as scrolled

col_width = 100
row_height = 20
readonly_color = "#919191"


class Account(scrolled.ScrolledPanel):
    windows = []

    def __init__(self, *args, **kwargs):
        """
        Will clear Consume.windows
        """
        super(Account, self).__init__(*args, **kwargs)

        Account.windows = []

        self.box = wx.BoxSizer(wx.VERTICAL)
        blank = wx.StaticText(self, size=(10, 20))
        Account.windows.append(blank)
        filter = Filter(self)
        Account.windows.append(filter)
        blank = wx.StaticText(self, size=(10, 20))
        Account.windows.append(blank)
        header = Header(self)
        Account.windows.append(header)
        list = List(self)
        Account.windows.append(list)
        self.box.AddMany(Account.windows)
        self.SetSizer(self.box)
        self.SetupScrolling()

    def get_window(tgt_type):
        for window in Account.windows:
            if type(window) is tgt_type:
                return window

    def layout(self, event=None):
        m.layout(self)
        if event is not None:
            event.Skip()
        return self


class Filter(wx.Panel):
    page_total = 0
    page_offset = 0
    page_limit = None

    def __init__(self, *args, **kwargs):
        """
        Will init Filter's constants
        """
        super(Filter, self).__init__(*args, **kwargs)

        Filter.page_total = 0
        Filter.page_offset = 0
        Filter.page_limit = None

        width = 50
        height = 25
        self.box = wx.BoxSizer(wx.HORIZONTAL)
        self.SetSizer(self.box)
        self.page_limit = wx.ComboBox(self, pos=(width, height), value="10", choices=["10", "20", "30", "40", "50", "60", "70", "80", "90", "100"], style=wx.CB_READONLY)
        Filter.page_limit = int(self.page_limit.GetValue())
        self.refresh = wx.Button(self, wx.ID_ANY, label="刷新", size=(width, height))
        self.page_info = wx.StaticText(self, wx.ID_ANY, label="", size=(width * 2, height), style=wx.ALIGN_CENTRE_HORIZONTAL)
        self.bt_top = wx.Button(self, wx.ID_ANY, label="首页", size=(width, height))
        self.bt_bottom = wx.Button(self, wx.ID_ANY, label="末页", size=(width, height))
        self.bt_pre_page = wx.Button(self, wx.ID_ANY, label="上一页", size=(width, height))
        self.bt_next_page = wx.Button(self, wx.ID_ANY, label="下一页", size=(width, height))
        self.box.AddMany([self.page_limit, self.refresh, self.page_info, self.bt_top, self.bt_bottom, self.bt_pre_page, self.bt_next_page])

        self.bind_evt()

    def layout(self, event=None):
        m.layout(self)
        if event is not None:
            event.Skip()
        return self

    def bind_evt(self):
        self.page_limit.Bind(wx.EVT_COMBOBOX, self.update_page_limit)
        self.refresh.Bind(wx.EVT_BUTTON, lambda x: Account.get_window(List).update_data())
        self.bt_pre_page.Bind(wx.EVT_BUTTON, self.pre_page)
        self.bt_next_page.Bind(wx.EVT_BUTTON, self.next_page)
        self.bt_top.Bind(wx.EVT_BUTTON, self.top_page)
        self.bt_bottom.Bind(wx.EVT_BUTTON, self.bottom_page)

    def update_page_limit(self, event=None):
        Filter.page_offset = 0
        Filter.page_limit = int(event.GetEventObject().GetValue())
        Account.get_window(List).update_data()

        if event is not None:
            event.Skip()

    def pre_page(self, event=None):
        if Filter.page_offset == 0:
            return
        new_offset = Filter.page_offset - Filter.page_limit
        if new_offset < 0:
            new_offset = 0
        Filter.page_offset = new_offset
        Account.get_window(List).update_data()

        if event is not None:
            event.Skip()

    def next_page(self, event=None):
        if Filter.page_offset + Filter.page_limit >= Filter.page_total:
            return
        Filter.page_offset += Filter.page_limit
        Account.get_window(List).update_data()

        if event is not None:
            event.Skip()

    def update_total(self, value, event=None):
        Filter.page_total = value
        self.page_info.SetLabel("共" + str(value) + "条")

        if event is not None:
            event.Skip()

    def top_page(self, event=None):
        Filter.page_offset = 0
        Account.get_window(List).update_data()

        if event is not None:
            event.Skip()

    def bottom_page(self, event=None):
        div = Filter.page_total // Filter.page_limit
        mod = Filter.page_total % Filter.page_limit
        if mod == 0:
            Filter.page_offset = (div - 1) * Filter.page_limit
        else:
            Filter.page_offset = div * Filter.page_limit
        Account.get_window(List).update_data()

        if event is not None:
            event.Skip()


class Header(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent)
        self.box = wx.BoxSizer(wx.HORIZONTAL)
        self.SetSizer(self.box)

        self.box.AddMany([wx.StaticText(self, label="账号", size=(col_width * 2, row_height), style=wx.ALIGN_CENTRE_HORIZONTAL)
                             , wx.StaticText(self, label="余额", size=(col_width, row_height), style=wx.ALIGN_CENTRE_HORIZONTAL)
                             , wx.StaticText(self, label="名称", size=(col_width, row_height), style=wx.ALIGN_CENTRE_HORIZONTAL)
                             , wx.StaticText(self, label="户头", size=(col_width, row_height), style=wx.ALIGN_CENTRE_HORIZONTAL)
                             , wx.StaticText(self, label="创建", size=(col_width * 2, row_height), style=wx.ALIGN_CENTRE_HORIZONTAL)
                             , wx.StaticText(self, label="更新", size=(col_width * 2, row_height), style=wx.ALIGN_CENTRE_HORIZONTAL)])

    def layout(self, event=None):
        m.layout(self)
        if event is not None:
            event.Skip()
        return self


class List(wx.Panel):
    def __init__(self, parent):
        wx.Panel.__init__(self, parent)
        self.box = wx.BoxSizer(wx.VERTICAL)
        self.SetSizer(self.box)

        self.update_data()

    def layout(self, event=None):
        m.layout(self)
        if event is not None:
            event.Skip()
        return self

    def update_data(self, event=None):
        """
        Layout()
        """
        self.box.Clear(delete_windows=True)
        cdt = ""
        sql = " SELECT a.id, a.number, a.name, a.ctime, a.utime, u.name user_name FROM account a " \
              " INNER JOIN user u ON u.id = a.user_id " \
              " ORDER BY u.id, a.number "
        sql += cdt
        sqlc = " SELECT COUNT(0) FROM (" + sql + ") "
        rs = m.query(sql=sql + " LIMIT ?, ? ", data=[Filter.page_offset, Filter.page_limit])
        count = m.query(sql=sqlc)[0][0]
        for r in rs:
            account_id = r[0]
            number = r[1]
            name = r[2]
            ctime = r[3]
            utime = r[4]
            user_name = r[5]
            self.box.Add(Row(self).load_data(account_id=account_id, number=number, name=name, ctime=ctime, utime=utime, user_name=user_name))
        Account.get_window(Filter).update_total(value=count)
        self.layout()

        if event is not None:
            event.Skip()


class Row(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent)

        self.key_codes = [-100, -100, -100, -100, -100, -100, -100, -100, -100, -100]

        self.box = wx.BoxSizer(wx.HORIZONTAL)
        self.SetSizer(self.box)

        self.account_id = None
        self.user_id = None

        font = wx.Font(pointSize=10, family=wx.FONTFAMILY_DEFAULT, style=wx.FONTSTYLE_NORMAL, weight=wx.FONTWEIGHT_BOLD, encoding=wx.FONTENCODING_SYSTEM)
        self.number = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(col_width * 2, row_height), style=wx.TE_PROCESS_ENTER)
        self.number.SetFont(font)
        self.number.Bind(wx.EVT_KEY_DOWN, self.number_shortcut)
        self.balance = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(col_width, row_height), style=wx.TE_PROCESS_ENTER | wx.TE_READONLY)
        self.balance.SetBackgroundColour(readonly_color)
        self.balance.SetFont(font)
        self.name = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(col_width, row_height))
        self.name.SetFont(font)
        self.user_name = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(col_width, row_height), style=wx.TE_PROCESS_ENTER)
        self.user_name.SetFont(font)
        self.user_name.Bind(wx.EVT_KILL_FOCUS, self.check_user_name)
        self.ctime = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(col_width * 2, row_height), style=wx.TE_PROCESS_ENTER | wx.TE_READONLY)
        self.ctime.SetFont(font)
        self.ctime.SetBackgroundColour(readonly_color)
        self.utime = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(col_width * 2, row_height), style=wx.TE_PROCESS_ENTER | wx.TE_READONLY)
        self.utime.SetFont(font)
        self.utime.SetBackgroundColour(readonly_color)
        self.box.AddMany([self.number, self.balance, self.name, self.user_name, self.ctime, self.utime])

    def check_user_name(self, event=None):
        rows = m.query(" SELECT id FROM user WHERE name = ? ", (event.GetEventObject().GetValue(),))
        if len(rows) == 0:
            wx.MessageBox("wrong user name", "Info", wx.OK | wx.ICON_INFORMATION)

        if event is not None:
            event.Skip()

    def layout(self, event=None):
        m.layout(self)
        if event is not None:
            event.Skip()
        return self

    def number_shortcut(self, event=None):
        key_code = event.GetKeyCode()
        print("key_code: " + str(key_code))

        self.key_codes.append(key_code)
        if len(self.key_codes) > 10:
            del self.key_codes[0]

        if self.key_codes[len(self.key_codes) - 3] == 308 and self.key_codes[len(self.key_codes) - 2] == 306 and self.key_codes[len(self.key_codes) - 1] == 83:
            self.save_or_update()
        elif self.key_codes[len(self.key_codes) - 3] == 308 and self.key_codes[len(self.key_codes) - 2] == 306 and self.key_codes[len(self.key_codes) - 1] == 13:
            children = self.GetParent().box.GetChildren()
            for i in range(0, len(children)):
                if children[i].GetWindow().GetId() == self.GetId():
                    self.GetParent().box.Insert(index=i, window=Row(self.GetParent()))
                    Account.get_window(List).layout()
                    break

        if event is not None:
            event.Skip()

    def load_data(self, account_id, number, name, ctime, utime, user_name):
        self.account_id = account_id
        self.number.SetValue(str(number))
        self.name.SetValue(name)
        self.ctime.SetValue(ctime)
        self.utime.SetValue(utime)
        self.user_name.SetValue(user_name)
        return self

    def save_or_update(self):
        if self.number.GetValue() == "":
            wx.MessageBox("empty account number", "Info", wx.OK | wx.ICON_INFORMATION)
            return
        sql = " SELECT id FROM user WHERE name = ? "
        rows = m.query(sql=sql, data=[self.user_name.GetValue()])
        if len(rows) == 0:
            wx.MessageBox("wrong user name", "Info", wx.OK | wx.ICON_INFORMATION)
            return
        self.user_id = rows[0][0]
        if self.account_id is None:
            sql = " INSERT INTO account (number, name, ctime, utime, user_id) " \
                  " VALUES (?, ?, DATETIME(CURRENT_TIMESTAMP,'localtime'), DATETIME(CURRENT_TIMESTAMP,'localtime'), ?) "
            m.dml([(sql, (self.number.GetValue(), self.name.GetValue(), self.user_id))])
        else:
            sql = " UPDATE account  SET utime = DATETIME(CURRENT_TIMESTAMP,'localtime') " \
                  ", number = ? " \
                  ", name = ? " \
                  ", user_id = ? " \
                  " WHERE id = ? "
            m.dml([(sql, (self.number.GetValue(), self.name.GetValue(), self.user_id, self.account_id))])
        self.GetParent().update_data()


def main():
    app = wx.App()
    frame = wx.Frame(None, title='Main', size=(800, 600))
    Account(parent=frame)
    frame.Center()
    frame.Show()
    app.MainLoop()


if __name__ == '__main__':
    main()
