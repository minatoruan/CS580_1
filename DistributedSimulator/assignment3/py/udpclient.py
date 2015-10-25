import socket, time
from datetime import datetime

def retrieve(arg):
    word = arg[1]
    if word == '': return ''
    else:
        HOST, PORT = 'localhost', arg[0]
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        sock.sendto(word, (HOST, PORT))
        if word == '!q': return ''
        
        len_msg = sock.recv(20)        
        return sock.recv(int(len_msg))

class Timer(object):
    def __init__(self, verbose=False):
        self.verbose = verbose

    def __enter__(self):
        self.start = time.time()
        return self

    def __exit__(self, *args):
        self.end = time.time()
        self.secs = self.end - self.start
        self.msecs = self.secs * 1000  # millisecs
        if self.verbose:
            print 'elapsed time: %f ms' % self.msecs
        
if __name__ == '__main__':
    from multiprocessing import Pool
    import sys
    port = int(sys.argv[1])
    
    sentence = 'After the signatures are folded and gathered they move into the bindery In the middle of last century there were still many trade binders standalone binding companies which did no printing specializing in binding alone At that time because of the dominance of letterpress printing typesetting and printing took place in one location and binding in a different factory When type was all metal a typical worth of book worth of type would be bulky fragile and heavy The less it was moved in this condition the better so printing would be carried out in the same location as the typesetting Printed sheets on the other hand could easily be moved Now because of increasing computerization of preparing a book for the printer the typesetting part of the job has flowed upstream where it is done either by separately contracting companies working for the publisher by the publishers themselves or even by the authors Mergers in the book manufacturing industry mean that it is now unusual to find a bindery which is not also involved in book printing and vice versa'
    words = sentence.split()
    pool = Pool(100)
   
    with Timer() as t:
        result = pool.map(retrieve, [(port, w) for w in words])
        for word, definition in zip(words, result):
            print 'Look up word: ', word
            print definition
    print "=> elasped lpush: %s s" % t.secs
    #print 'Sentences:', sentence 
    #print 'Requesting port:', port        
    #print 'Number of words to be requested:', len(words)
    #print 'Number of definitions to be recieved:', len(result)

    #send this message to close the server
    #retrieve((port, '!q'))

    pool.close()
    pool.join()
    
