import socket
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

class Timer:
    def __init__(self):
        pass
    def __enter__(self):
        self.start = datetime.now()
    def __exit__(self, type, value, traceback):
        self.end = datetime.now()
    def duration(self):
        return self.end - self.start
        
if __name__ == '__main__':
    from multiprocessing import Pool
    import sys
    port = 9002

    newwords = ['&MOUNTEBANK:Definition:	(noun) A hawker of quack medicines who attracts customers with stories, jokes, or tricks.',
                    '&mountebank:Definition:	(noun) A hawker of quack medicines who attracts customers with stories, jokes, or tricks.',
                    '&Glucophage:Definition:	A trademark for the drug metformin.',
                    '&Antioxidant:Definition:	Any substance that reduces the damage caused by oxidation, such as the harm caused by free radicals.']
                
    pool = Pool(4)

    timer = Timer()
    with timer:
        result = pool.map(retrieve, [(port, w) for w in newwords])
    
    for word, definition in zip(newwords, result):
        print 'Add word: ', word
        print definition

    print 'Requesting port:', port
    print 'Number of words to be requested:', len(newwords)
    print 'Number of definitions to be recieved:', len(result)
    print 'Elapsed time: ', timer.duration()

    pool.close()
    pool.join()
    
