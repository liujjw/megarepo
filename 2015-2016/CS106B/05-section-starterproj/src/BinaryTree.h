/*
 * CS 106B Section 6
 * See BinaryTree.cpp for implementation of each member function.
 */

#ifndef _binarytree_h
#define _binarytree_h

#include <fstream>
#include <iostream>
#include <string>
using namespace std;

// A TreeNode is one node in a binary tree of integers.
struct TreeNode {
    int data;          // data stored at this node
    TreeNode* left;    // pointer to left subtree
    TreeNode* right;   // pointer to right subtree

    // Constructs a node with the given data and links.
    TreeNode(int data, TreeNode* left  = NULL,
                       TreeNode* right = NULL) {
        this->data = data;
        this->left = left;
        this->right = right;
    }

    bool isLeaf() {
        return left == NULL && right == NULL;
    }
};


class BinaryTree {
public:
    // constructor and destructor
    BinaryTree();
    ~BinaryTree();

    // member functions (methods)
    int height() const;
    int countLeftNodes();
    bool isBalanced();
    bool isBST();
    void removeLeaves();
    void completeToLevel(int k);
    void tighten();
    void limitPathSum(int max);
    void tighten();

    int height(TreeNode* node) const;
    void tighten(TreeNode*& node);
    void removeLeaves(TreeNode*& node);
    int countLeftNodes(TreeNode* m_root) const;
    void clear();
    bool isBalanced(TreeNode* node) const;
    void clear(TreeNode* node);
    void add(int value);
    void add(TreeNode*& node, int value);
    string toString();
    string toString(TreeNode* node);
    void completeToLevel(int k, TreeNode*& node, int levelSoFar);
    bool isBST(TreeNode* node, TreeNode* prev) const;
    void limitPathSum(int max, TreeNode*& node, int sumSoFar);

private:
    // member variables (fields)
    TreeNode* m_root;   // NULL if list is empty
};

// overloaded operators
ostream& operator <<(ostream& out, BinaryTree& root);

#endif







