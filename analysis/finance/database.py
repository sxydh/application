import wx
import main as m


class Database(wx.Panel):

    def __init__(self, *args, **kwargs):
        super(Database, self).__init__(*args, **kwargs)

        self.key_codes = [-100, -100, -100, -100, -100, -100, -100, -100, -100, -100]

        box = wx.BoxSizer(wx.VERTICAL)
        self.input = wx.TextCtrl(self, id=wx.ID_ANY, style=wx.TE_MULTILINE)
        self.output = wx.StaticText(self, label="", style=wx.ALIGN_LEFT)
        box.Add(self.input, wx.ID_ANY, wx.EXPAND)
        box.Add(self.output, wx.ID_ANY, wx.EXPAND)
        self.SetSizer(box)

        self.input.Bind(wx.EVT_KEY_DOWN, self.execute_input)

    def execute_input(self, event=None):
        key_code = event.GetKeyCode()
        print("key_code: " + str(key_code))
        text_ctrl = event.GetEventObject()

        self.key_codes.append(key_code)
        if self.key_codes[len(self.key_codes) - 2] == 308 and self.key_codes[len(self.key_codes) - 1] == 65:
            text_ctrl.SetSelection(0, len(text_ctrl.GetValue()))
        elif self.key_codes[len(self.key_codes) - 2] == 308 and self.key_codes[len(self.key_codes) - 1] == 13:
            sql = self.input.GetStringSelection()
            result = m.execute(sql=sql)
            self.output.SetLabel(str('\n'.join('{}: {}'.format(*k) for k in enumerate(result))))
            return

        if event is not None:
            event.Skip()


def main():
    app = wx.App()
    frame = wx.Frame(None, title='Main', size=(800, 600))
    Database(parent=frame)
    frame.Center()
    frame.Show()
    app.MainLoop()


if __name__ == '__main__':
    main()
