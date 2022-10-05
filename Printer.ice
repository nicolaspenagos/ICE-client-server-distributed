module Demo
{
    interface Callback
    {
        void response(string s);
    }

    interface Printer
    {
        void printString(string s, Callback* cl);
    }

}