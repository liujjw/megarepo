/*
 * CS 106B Section 06 Problems
 */
#include <iostream>
#include <string>
#include "console.h"
#include "BinaryTree.h"
using namespace std;

void testHeight();
void testIsBST();
void testLimitPathSum();
void testIsBalanced();
void testCompleteToLevel();
void testCountLeftNodes();
void testRemoveLeaves();
void testTighten();

int main() {
    setConsoleSize(750, 500);
    setConsoleFont("Monospaced-Bold-14");
    setConsoleEcho(true);

    // run tests
    testHeight();
    testIsBST();
    testLimitPathSum();
    testIsBalanced();
    testCompleteToLevel();
    testCountLeftNodes();
    testRemoveLeaves();
    testTighten();


    return 0;
}

void testHeight() {
    BinaryTree tree;
    tree.add(7);
    tree.add(5);
    tree.add(3);
    cout << "height: " << tree.height() << " should be 2" << endl;
}

void testIsBST() {
    BinaryTree tree;
    tree.add(8);
    tree.add(4);
    tree.add(9);
    string test1 = tree.isBST() ? "true" : "false";
    cout << "isBST: " << test1 << " should be true" << endl;
    tree.add(10);
    string test2 = tree.isBST() ? "true" : "false";
    cout << "isBST: " << test2 << " should be false" << endl;
}

void testLimitPathSum() {
    BinaryTree tree;
    tree.add(1);
    tree.add(2);
    tree.add(4);
    tree.add(5);
    tree.limitPathSum(5);
    cout << "limitPathSum: " << tree << " should be (1, 2, 4)" << endl;
}

void testIsBalanced() {
    BinaryTree tree;
    tree.add(8);
    tree.add(4);
    tree.add(9);
    string test = tree.isBalanced() ? "true" : "false";
    cout << "isBalanced: " << test << " should be true" << endl;
}

void testCompleteToLevel() {
    BinaryTree tree;
    tree.add(7);
    tree.add(3);
    tree.add(9);
    tree.add(1);
    tree.add(6);
    tree.add(4);
    tree.add(8);
    tree.completeToLevel(3);
    cout << "completeToLevel: " << tree << " should be (7, (3, (1, 8, /), 4), (9, 6, -1))" << endl;
}

void testCountLeftNodes() {
    BinaryTree tree;
    tree.add(2);
    tree.add(1);
    tree.add(4);
    tree.add(6);
    tree.add(5);
    cout << "countLeftNodes: " << tree.countLeftNodes() << " should be 3" << endl;
}

void testRemoveLeaves() {
    BinaryTree tree;
    tree.add(7);
    tree.add(3);
    tree.add(9);
    tree.add(1);
    tree.add(4);
    tree.add(6);
    tree.add(8);
    tree.add(0);

    cout << "removeLeaves: " << tree << endl;
    tree.removeLeaves();
    cout << "removeLeaves: " << tree << endl;
    tree.removeLeaves();
    cout << "removeLeaves: " << tree << endl;
    tree.removeLeaves();
    cout << "removeLeaves: " << tree << endl;
    tree.removeLeaves();
    cout << "removeLeaves: " << tree << endl;

}

void testTighten() {

}
