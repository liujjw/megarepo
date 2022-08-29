#include "encoding.h"
#include "map.h"
#include "huffmanutil.h"
#include "filelib.h"
#include "bitstream.h"
#include "pqueue.h"
#include "HuffmanNode.h"
#include <iostream>

Map<int, int> buildFrequencyTable(istream& input) {
    rewindStream(input);
    Map<int, int> freqTable;
    while(true) {
        int character = input.get();
        if(character == EOF) {
            freqTable.add(PSEUDO_EOF, 1);
            break; // extra 0s after eof to complete a byte
        }
        if(!freqTable.containsKey(character)) {
            freqTable.add(character, 1);
        } else {
            int originalValue = freqTable.get(character);
            freqTable.add(character, originalValue + 1);
        }
    }
    return freqTable;
}

HuffmanNode* buildEncodingTree(const Map<int, int>& freqTable) {
    PriorityQueue<HuffmanNode*> pqueue;
    for(int key : freqTable) {
        int value = freqTable.get(key);
        HuffmanNode* node = new HuffmanNode(key, value);
        pqueue.enqueue(node, value);
    }
    while(pqueue.size() > 1) {
        HuffmanNode* node1 = pqueue.dequeue();
        HuffmanNode* node2 = pqueue.dequeue();
        HuffmanNode* newNode = new HuffmanNode(NOT_A_CHAR, node1->count + node2->count,
                                               node1, node2);
        pqueue.enqueue(newNode, node1->count + node2->count);
    }
    return pqueue.dequeue();
}

Map<int, string> buildEncodingMap(HuffmanNode* encodingTree) {
    Map<int, string> encodingMap;
    traverseHuffTree(encodingMap, encodingTree, "");  // separate recursive function
    return encodingMap;
}
void traverseHuffTree(Map<int, string>& encodingMap, const HuffmanNode* node, string completeBits) {
    if(node->zero == NULL && node->one == NULL) {
        encodingMap.put(node->character, completeBits);
    } else {
        traverseHuffTree(encodingMap, node->zero, completeBits + "0");
        traverseHuffTree(encodingMap, node->one, completeBits + "1");
    }
}


void encodeData(istream& input, const Map<int, string>& encodingMap, obitstream& output) {
    rewindStream(input);
    while(true) {
        int inChar = (int) input.get();
        if(inChar == EOF) { // not sure about eof values
            inChar = PSEUDO_EOF;
        }
        string outCharBytes = encodingMap.get(inChar);
        for(int i = 0; i < outCharBytes.size(); i++) {
            int bit = (outCharBytes[i] == '0' ? 0 : 1); // encoding map maps chars to string of binary
            output.writeBit(bit);
        }
        if(inChar == PSEUDO_EOF) { // stop at eof, dont encode extra 0s at the end, will be added as only 0s
            break;
        }
    }
}

void decodeData(ibitstream& input, HuffmanNode* encodingTree, ostream& output) {
    // traverses from root after recursive call completes a byte on output
    // at the end of every file, even in compressed, there is empty 0s that fill up
    // a complete byte
    bool foundEOF = false;
    while(!foundEOF) {
        decodeDataHelper(input, encodingTree, output, foundEOF);
    }
}
void decodeDataHelper(ibitstream& input, HuffmanNode* node, ostream& output, bool& flag) {
    if(node->zero == NULL && node->one == NULL) {
        int byte = node->character; // can be EOF character, so int
        if(byte == PSEUDO_EOF) { // eofs are added auto when file is closed
            flag = true;
            return;
        }
        output.put((char) byte);
    } else {
        int bit = input.readBit(); // reads a bit returns as int
        if(bit == 0) {
            decodeDataHelper(input, node->zero, output, flag);
        } else {
            decodeDataHelper(input, node->one, output, flag);
        }
    }
}

void compress(istream& input, obitstream& output) {
    if(input.fail()) {
        return;
    }
    Map<int, int> freqTable = buildFrequencyTable(input);
    HuffmanNode* encodingTreeRoot = buildEncodingTree(freqTable);
    Map<int, string> encodingMap = buildEncodingMap(encodingTreeRoot);
    output << freqTable; // writing freq table first
    encodeData(input, encodingMap, output);
    freeTree(encodingTreeRoot);
}

void decompress(ibitstream& input, ostream& output) {
    if(input.fail()) {
        return;
    }
    Map<int, int> freqTable;
    input >> freqTable;
    HuffmanNode* encodingTreeRoot = buildEncodingTree(freqTable); // build tree from header
    decodeData(input, encodingTreeRoot, output);
    freeTree(encodingTreeRoot);
}

void freeTree(HuffmanNode* node) {
    if(node == NULL) {
        return;
    } else if(node->isLeaf()) {
        delete node;
    } else {
        freeTree(node->one);
        freeTree(node->zero);
        delete node;
    }
}
