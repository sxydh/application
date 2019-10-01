import wx
import re
import datetime
import main as m
import wx.lib.scrolledpanel as scrolled

col_width = 100
row_height = 20
readonly_color = "#919191"


class User(wx.Panel):
    windows = []

    def __init__(self, *args, **kwargs):
        """
        Will clear Consume.windows
        """
        super(User, self).__init__(*args, **kwargs)

        User.windows = []

        self.box = wx.BoxSizer(wx.VERTICAL)
        blank = wx.StaticText(self, size=(10, 20))
        User.windows.append(blank)
        filter = Filter(self)
        User.windows.append(filter)
        blank = wx.StaticText(self, size=(10, 20))
        User.windows.append(blank)
        header = Header(self)
        User.windows.append(header)
        list = List(self)
        User.windows.append(list)
        self.box.AddMany(User.windows)
        self.SetSizer(self.box)

    def get_window(tgt_type):
        for window in User.windows:
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
        self.refresh.Bind(wx.EVT_BUTTON, lambda x: User.get_window(List).update_data())
        self.bt_pre_page.Bind(wx.EVT_BUTTON, self.pre_page)
        self.bt_next_page.Bind(wx.EVT_BUTTON, self.next_page)
        self.bt_top.Bind(wx.EVT_BUTTON, self.top_page)
        self.bt_bottom.Bind(wx.EVT_BUTTON, self.bottom_page)

    def update_page_limit(self, event=None):
        Filter.page_offset = 0
        Filter.page_limit = int(event.GetEventObject().GetValue())
        User.get_window(List).update_data()

        if event is not None:
            event.Skip()

    def pre_page(self, event=None):
        if Filter.page_offset == 0:
            return
        new_offset = Filter.page_offset - Filter.page_limit
        if new_offset < 0:
            new_offset = 0
        Filter.page_offset = new_offset
        User.get_window(List).update_data()

        if event is not None:
            event.Skip()

    def next_page(self, event=None):
        if Filter.page_offset + Filter.page_limit >= Filter.page_total:
            return
        Filter.page_offset += Filter.page_limit
        User.get_window(List).update_data()

        if event is not None:
            event.Skip()

    def update_total(self, value, event=None):
        Filter.page_total = value
        self.page_info.SetLabel("共" + str(value) + "条")

        if event is not None:
            event.Skip()

    def top_page(self, event=None):
        Filter.page_offset = 0
        User.get_window(List).update_data()

        if event is not None:
            event.Skip()

    def bottom_page(self, event=None):
        div = Filter.page_total // Filter.page_limit
        mod = Filter.page_total % Filter.page_limit
        if mod == 0:
            Filter.page_offset = (div - 1) * Filter.page_limit
        else:
            Filter.page_offset = div * Filter.page_limit
        User.get_window(List).update_data()

        if event is not None:
            event.Skip()


class Header(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent)
        self.box = wx.BoxSizer(wx.HORIZONTAL)
        self.SetSizer(self.box)

        self.box.AddMany([
            wx.StaticText(self, label="姓名", size=(col_width, row_height), style=wx.ALIGN_CENTRE_HORIZONTAL)
            , wx.StaticText(self, label="创建", size=(col_width, row_height), style=wx.ALIGN_CENTRE_HORIZONTAL)
            , wx.StaticText(self, label="更新", size=(col_width, row_height), style=wx.ALIGN_CENTRE_HORIZONTAL)])

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
        sql = " SELECT u.id, u.name, u.ctime, u.utime FROM user u " \
              " ORDER BY u.name, u.id "
        sql += cdt
        sqlc = " SELECT COUNT(0) FROM (" + sql + ") "
        rs = m.query(sql=sql + " LIMIT ?, ? ", data=[Filter.page_offset, Filter.page_limit])
        count = m.query(sql=sqlc)[0][0]
        for r in rs:
            id = r[0]
            name = r[1]
            ctime = r[2]
            utime = r[3]
            self.box.Add(Row(self).load_data(id=id, name=name, ctime=ctime, utime=utime))
        User.get_window(Filter).update_total(value=count)
        self.layout()

        if event is not None:
            event.Skip()


class Row(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent)

        self.key_codes = [-100, -100, -100, -100, -100, -100, -100, -100, -100, -100]

        self.box = wx.BoxSizer(wx.HORIZONTAL)
        self.SetSizer(self.box)

        self.id = None

        font = wx.Font(pointSize=10, family=wx.FONTFAMILY_DEFAULT, style=wx.FONTSTYLE_NORMAL, weight=wx.FONTWEIGHT_BOLD, encoding=wx.FONTENCODING_SYSTEM)
        self.name = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(col_width, row_height), style=wx.TE_PROCESS_ENTER)
        self.name.SetFont(font)
        self.ctime = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(col_width * 2, row_height), style=wx.TE_PROCESS_ENTER | wx.TE_READONLY)
        self.ctime.SetFont(font)
        self.ctime.SetBackgroundColour(readonly_color)
        self.utime = wx.TextCtrl(self, id=wx.ID_ANY, value="", size=(col_width * 2, row_height), style=wx.TE_PROCESS_ENTER | wx.TE_READONLY)
        self.utime.SetFont(font)
        self.utime.SetBackgroundColour(readonly_color)
        self.box.AddMany([self.name, self.ctime, self.utime])

    def layout(self, event=None):
        m.layout(self)
        if event is not None:
            event.Skip()
        return self

    def name_shortcut(self, event=None):
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
                    User.get_window(List).layout()
                    break

        if event is not None:
            event.Skip()

    def load_data(self, id, name, ctime, utime):
        self.id = id
        self.name.SetValue(name)
        self.ctime.SetValue(ctime)
        self.utime.SetValue(utime)
        return self

    def save_or_update(self):
        if self.id is None:
            sql = " INSERT INTO user (name, ctime, utime) " \
                  " VALUES (?, DATETIME(CURRENT_TIMESTAMP,'localtime'), DATETIME(CURRENT_TIMESTAMP,'localtime')) "
            m.dml([(sql, (self.name.GetValue()))])
        else:
            sql = " UPDATE user SET utime = DATETIME(CURRENT_TIMESTAMP,'localtime') " \
                  ", name = ? " \
                  " WHERE id = ? "
            m.dml([(sql, (self.name.GetValue(), self.id))])
        self.GetParent().update_data()


def main():
    app = wx.App()
    frame = wx.Frame(None, title='Main', size=(800, 600))
    User(parent=frame)
    frame.Center()
    frame.Show()
    app.MainLoop()


if __name__ == '__main__':
    main()
