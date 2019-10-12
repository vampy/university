#include "../os.hpp"
#include "../udp.hpp"
#include <thread>
#include <chrono>
#include <mutex>
#include <atomic>
#include <vector>
#include <cstring>
#include <ncurses.h>
// Executable is kinda huge, because of all the libraries, TODO optimize

/*
send to broadcast address
multiple listens on that broadcast address
keep a list of clients

posix threads
one send broadcast thread
- sleep(3)
- send(time query)
another send thread
- sleep(10)
- send(date query)

another receive thread
another main program:
print the active list of peers

struct Peer {
struct sockaddr_in (IP si Port)
int counter = 0; (increment when send timequery, deincrement when sending data and when the counter is 3 delete the peer
}

specify uses establish
*/

using namespace std;

const uint16_t PORT = 7777;
const uint16_t MAX_MESSAGE = 33;
string NBCAST;
atomic<int32_t> threads_count;
atomic<bool> exit_program;

#define WAIT_FOR_THREADS false // TODO fix this
#define USE_NCURSES true
#define SLEEP_THREAD(_amount)            \
    if (exit_program) break;             \
    this_thread::sleep_for(_amount);     \
    if (exit_program) break;

struct Peer
{
    sockaddr_in address;
    string time; // use time for malformed data
    string date;
    atomic<int32_t> counter;

    Peer(sockaddr_in *address)
    {
        counter = 1;
        time = "";
        date = "";
        memcpy(&(this->address), address, sizeof(sockaddr_in));
    }
};
class Peers
{
public:
    vector<Peer*> peers;
    vector<Peer*> wrong_peers;
    mutex mtx;
    int32_t found_position = -1;

    ~Peers()
    {
        for (uint32_t i = 0; i < peers.size(); i++)
        {
            Peer *peer = peers.at(i);
            DELETE(peer);
        }
        for (uint32_t i = 0; i < wrong_peers.size(); i++)
        {
            Peer *peer = wrong_peers.at(i);
            DELETE(peer);
        }
    }

    bool exists(sockaddr_in *address)
    {
        mtx.lock();
        for (uint32_t i = 0; i < peers.size(); i++)
        {
            Peer *peer = peers.at(i);
            if (memcmp(address, &(peer->address), sizeof(sockaddr_in)) == 0)
            {
                found_position = i;
                mtx.unlock();
                return true;
            }
        }
        mtx.unlock();

        return false;
    }

    Peer *getFoundPeer()
    {
        assert(found_position != -1);
        return peers.at(found_position);
    }

    void add(Peer *peer)
    {
        mtx.lock();
        peers.push_back(peer);
        mtx.unlock();
    }
    void incrementCounters()
    {
        mtx.lock();
        vector<int> remove_positions; // keep track of all positions we need to remove
        for (uint32_t i = 0; i < peers.size(); i++)
        {
            Peer *peer = peers.at(i);
            peer->counter++;
            if (peer->counter > 3)
            {
                remove_positions.push_back(i);
            }
        }
        for (int pos: remove_positions)
        {
            peers.erase(peers.begin() + pos);
        }
        mtx.unlock();
    }

    void addWrongPeer(Peer *peer)
    {
        mtx.lock();
        wrong_peers.push_back(peer);
        mtx.unlock();
    }
};

void int_handle(int signal)
{
    if (USE_NCURSES) endwin();
    exit_program = true;
    cout << "CTRL+C pressed" << endl;
    cout << "Waiting for threads to finish" << endl;
    if (WAIT_FOR_THREADS)
        while (threads_count > 2); // wait for threads
        // will continue into main
    else
        exit(0);
}

void handle_send_time(SafeSocketUDP *socket_send, Peers *peers)
{
    threads_count++;
    if (!USE_NCURSES) cout << "Starting thread: send time" << endl;

    auto sleep_time = chrono::seconds(3);
    string message = "TIMEQUERY";
    while (true)
    {
        SLEEP_THREAD(sleep_time)
        if (!USE_NCURSES) cout << "Sending: TIMEQUERY\0 (string)" << endl;

        socket_send->sendString(message);
        peers->incrementCounters();
    }

    threads_count--;
}

void handle_send_date(SafeSocketUDP *socket_send)
{
    threads_count++;
    if (!USE_NCURSES) cout << "Starting thread: send date" << endl;

    auto sleep_time = chrono::seconds(10);
    string message = "DATEQUERY";
    while (true)
    {
        SLEEP_THREAD(sleep_time)
        if (!USE_NCURSES) cout << "Sending: DATEQUERY\0 (string)" << endl;

        socket_send->sendString(message);
    }

    threads_count--;
}

void handle_receive(Peers *peers)
{
    threads_count++;
    if (!USE_NCURSES) cout << "Starting thread: receive" << endl;
    auto socket_receive = SafeSocketUDP();
    socket_receive.open()
            ->setOptionReuseAddress()
            ->setAddress(PORT) // listen to local port 7777
            ->bind();
    char message[MAX_MESSAGE];
    while (true)
    {
        if (exit_program) break;
        socket_receive.receive<char*>(message, MAX_MESSAGE);

        if (!USE_NCURSES)
            cout << "message = " << message
                 << ", From = " << socket_receive.getFromIPString() << ":" << socket_receive.getFromPort() << endl;

        socket_receive.setAddressAsFromAddress();
        bool exists = peers->exists(socket_receive.getFromAddressIPv4()), valid = false;
        if (strncmp("TIMEQUERY", message, 9) == 0) // received timequery
        {
            // SLEEP_THREAD(chrono::seconds(rand() % 5)); // have different times
            valid = true;
            string time = "TIME " + Time::getTime();
            if (!USE_NCURSES) cout << "type TIMEQUERY: " << time << endl;

            socket_receive.sendString(time);
        }
        else if (strncmp("DATEQUERY", message, 9) == 0) // received datequery
        {
            valid = true;
            if (!USE_NCURSES) cout << "type DATEQUERY" << endl;

            string date = "DATE " + Time::getDate();
            socket_receive.sendString(date);
        }
        else if (strncmp("TIME ", message, 5) == 0) // received time
        {
            valid = true;
            if (!USE_NCURSES) cout << "data TIME" << endl;
            if (exists) // update time
            {
                Peer *peer = peers->getFoundPeer();
                peer->time = message;
                peer->counter--; // got response from peer
            }
        }
        else if (strncmp("DATE ", message, 5) == 0) // received date
        {
            valid = true;
            if (!USE_NCURSES) cout << "data DATE" << endl;
            if (exists) // update date
            {
                Peer *peer = peers->getFoundPeer();
                peer->date = message;
            }
        }
        else
        {
            if (!USE_NCURSES) cout << "Invalid message data: " << message << endl;

            // log malformed data
            Peer *peer = new Peer(socket_receive.getFromAddressIPv4());
            peer->time = message;
            peers->addWrongPeer(peer);
        }

        if (!exists && valid) // ADD peer
        {
            if (!USE_NCURSES) cout << "<<<<<<<<<<<<<<< ADDING PEER >>>>>>>>>>>" << endl;
            peers->add(new Peer(socket_receive.getFromAddressIPv4()));
        }

        if (!USE_NCURSES) cout << endl << endl;
        socket_receive.setAddress(PORT);
    }

    threads_count--;
}

void handle_main(Peers *peers)
{
    threads_count++;
    if (!USE_NCURSES)
    {
        cout << "Starting thread: main" << endl;
    }
#if !USE_NCURSES
#define printw printf
#endif

    chrono::milliseconds sleep_time;
    if (USE_NCURSES)
    {
        initscr();
        sleep_time = chrono::milliseconds(50);
    }
    else
    {
        sleep_time = chrono::milliseconds(1000);
    }
    while (true)
    {
        SLEEP_THREAD(sleep_time)
        if (USE_NCURSES) clear();

        printw("------------------------------------------------------------------------\nPeers:\n");
        peers->mtx.lock();
        for (Peer *peer : peers->peers)
        {
            printw("\t%s:%d\t\t%s %s, counter = %d\n\n",
                  SafeSocket::getIPString(&(peer->address)).c_str(),
                  SafeSocket::getPort(&(peer->address)),
                  peer->time.c_str(),
                  peer->date.c_str(),
                  (int32_t)peer->counter);

        }
        peers->mtx.unlock();
        printw("Malformed Data:\n");
        for (Peer *peer : peers->wrong_peers)
        {
            printw("\t%s:%d - %s\n",
                  SafeSocket::getIPString(&(peer->address)).c_str(),
                  SafeSocket::getPort(&(peer->address)),
                  peer->time.c_str());

        }
        if (USE_NCURSES) refresh();
    }
    if (USE_NCURSES)
    {
        endwin();
    }

    threads_count--;
}

int main(int argc, char *argv[])
{
    // TODO set NBCAST argument from command line
    if (argc >= 2) // set from command line
    {
        NBCAST = argv[1];
    }
    else // use default
    {
        NBCAST = "192.168.1.255";
    }
    cout << "NBCAST = " << NBCAST << endl;
    Peers *peers = new Peers();

    threads_count = 0;
    exit_program = false;
    signal(SIGINT, int_handle);
    SafeSocketUDP *socket_send = new SafeSocketUDP();
    socket_send->open()
            ->setOptionReuseAddress()
            ->setOptionBroadcast()
            ->setAddress(PORT) // listen to local port 7777
            ->bind()
            ->setAddress(NBCAST.c_str(), PORT); // send to broadcast address

    auto   thread_send_time = thread(handle_send_time, socket_send, peers),
           thread_send_date = thread(handle_send_date, socket_send),
           thread_receive = thread(handle_receive, peers),
           thread_main = thread(handle_main, peers);

    thread_send_time.join();
    thread_send_date.join();
    thread_receive.join();
    thread_main.join();

    DELETE(socket_send);
    DELETE(peers);
    return 0;
}
