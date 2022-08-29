/*
 * CS 106B Section 6
 * BinaryTree.cpp implements the BinaryTree class behavior
 * declared in BinaryTree.h.
 */
#include "BinaryTree.h"
#include "strlib.h"
using namespace std;


BinaryTree::BinaryTree() {
    m_root = NULL;
}

BinaryTree::~BinaryTree() {
    clear();
}

void BinaryTree::clear() {
    clear(m_root);
    m_root = NULL;
}

void BinaryTree::clear(TreeNode* node) {
    if (node != NULL) {
        clear(node->left);
        clear(node->right);
        delete node;
    }
}


void BinaryTree::add(int value) {
    add(m_root, value);
}

// Creates a balanced binary tree for simplicity
void BinaryTree::add(TreeNode*& node, int value) {
    if (node == NULL) {
        node = new TreeNode(value);
    } else {
        if (height(node->left) > height(node->right)) {
            add(node->right, value);
        } else {
            add(node->left, value);
        }
    }
}


int BinaryTree::height() const{
    return height(m_root);
}

int BinaryTree::height(TreeNode* node) const{
    if (node == NULL) {
        return 0;
    } else {
        return 1 + max(height(node->left), height(node->right));
    }
}

bool BinaryTree::isBST() {
    TreeNode* prev = NULL;
    return isBST(m_root, prev);
}

bool BinaryTree::isBST(TreeNode* node, TreeNode* prev) const {
    if(node == NULL) {
        return true;
    } else if(!isBST(node->left, prev) ||
              (prev && node->data <= prev->data)) { // check all lefts
        return false;
    } else {
        prev = node;
        return isBST(node->right, prev); // check all rights
    }
}

/*bool BinaryTree::isBST(TreeNode* node) {
 *  if(node == NULL) {
 *      return false;
 *  }
    if((node->left == NULL && node->right == NULL)) {
        return true;
    } else if(isBST(node->left) && isBST(node->right)) {
        return (node->left->data < node->data &&
                node->right->data > node->data);
    } else if(isBST(node->left)) {
        return (node->left->data < node->data);
    } else {
        return (node->right->data > node->data);
    }
}*/

void BinaryTree::limitPathSum(int max) {
    limitPathSum(max, m_root, 0);
}

void BinaryTree::limitPathSum(int max, TreeNode*& node, int sumSoFar) {
    if(node != NULL) {
        sumSoFar += node->data;
        if(sumSoFar == max) {
            clear(node);
            node = NULL;
        } else {
            limitPathSum(max, node->left, sumSoFar);
            limitPathSum(max, node->right, sumSoFar);
        }
    }
}

bool BinaryTree::isBalanced() {
    return isBalanced(m_root);
}

bool BinaryTree::isBalanced(TreeNode* node) const {
    if(node == NULL) {
        return true;
    } else if(isBalanced(node->left) && isBalanced(node->right)) {
        return (abs(height(node->left) - height(node->right)) <= 1);
    }
    return false;
}

void BinaryTree::completeToLevel(int k) {
    completeToLevel(k, m_root, 0);
}

void BinaryTree::completeToLevel(int level, TreeNode*& node, int levelSoFar) {
    if(node != NULL && levelSoFar <= level){
        if(node->left == NULL) {
            node->left = new TreeNode(-1, NULL);
        }
        if(node->right == NULL) {
            node->right = new TreeNode(-1, NULL);
        }
        completeToLevel(level, node->left, levelSoFar + 1);
        completeToLevel(level, node->right, levelSoFar + 1);
    }
}

/*
void BinaryTree::completeToLevel(int k) { if (k < 1) {
throw k; }
completeToLevel(root, k, 1); }
void BinaryTree::completeToLevel(TreeNode*& node, int k, int level) { if (level <= k) {
} }
if (node == NULL) {
node = new TreeNode(-1);
}
completeToLevel(node->left, k, level + 1); completeToLevel(node->right, k, level + 1);
*/

int BinaryTree::countLeftNodes()  {
    return countLeftNodes(m_root);
}

int BinaryTree::countLeftNodes(TreeNode* node) const {
    if(node == NULL) {
        return 0;
    } else {
        int countSoFar = 0;
        if(node->left != NULL) {
            countSoFar++;
        }
        return countSoFar + countLeftNodes(node->left) +
                            countLeftNodes(node->right);
    }
}

/*
 * int BinaryTree::countLeftNodes(TreeNode* node) const {
    if(node == NULL) {
        return 0;
    } else if(node->left == NULL) {
        return countLeftNodes(node->right);
    } else {
        return 1 + countLeftNodes(node->left) +
                    countLeftNodes(node-right);
    }
}
*/

string BinaryTree::toString() {
    return toString(m_root);
}


string BinaryTree::toString(TreeNode* node) {
    if (node == NULL) {
        return "/";
    } else if (node->left == NULL && node->right == NULL) {
        return integerToString(node->data);
    } else {
        return "(" + integerToString(node->data) + ", "
                + toString(node->left) + ", " + toString(node->right) + ")";
    }
}


ostream& operator <<(ostream& out, BinaryTree& root) {
    out << root.toString() << endl;
    return out;
}

void BinaryTree::removeLeaves() {
    removeLeaves(m_root);
}

void BinaryTree::removeLeaves(TreeNode*& node) {
    if(node != NULL) {
        if(node->left == NULL && node->right == NULL) {
            delete node;
            node = NULL;
        } else {
            removeLeaves(node->left);
            removeLeaves(node->right);
        }
    }
}

void BinaryTree::tighten() {
    tighten(m_root);
}

void BinaryTree::tighten(TreeNode*& node) {
    if(node != NULL) {
        if(node->right == NULL && node->left != NULL) {
            TreeNode* tmp = node;
            node = node->left;
            delete tmp;
            tighten(node);
        } else if(node->left == NULL && node->right != NULL) {
            TreeNode* tmp = node;
            node = node->right;
            delete tmp;
            tighten(node);
        }
        tighten(node->left);
        tighten(node->right);
    }
}
