using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace chat.model
{   
    public class Message
    {
    private User sender, receiver;
    private String message;

    public Message(User sender, User receiver, string message)
    {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public User Sender
    {
        get { return sender; }
    }

    public User Receiver
    {
        get { return receiver; }
    }

    public string Text
    {
        get { return message; }
    }

    public override string ToString()
    {
        return string.Format("Sender: {0}, Receiver: {1}, Message: {2}", sender, receiver, message);
    }
    }
}
