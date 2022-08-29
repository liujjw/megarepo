int LinkedList::min() const {
	




	if(root == NULL) {
		throw "List is empty";
	}
	LinkedList* cur = root;
	int min = cur->data;
	while(root->next != NULL) {
		cur = cur->next;
		if(cur->data < min) {
			min = cur->data;
		}
	}
	return min;
}

// nondecreasing order
// recursively and non-recursively works
bool LinkedList::isSorted() const {
	if(front == NULL) {
		return true;
	} else {
		int listValue = front->data;
		LinkedList* next = front->next;
		if(next == NULL) {
			return true;
		}
		if(next->data < listValue) {
			return false;
		}
		return next.isSorted();
	}

}

  bool LinkedList::isSorted() const {
      if (m_front != NULL) {
          ListNode* current = m_front;
          while (current->next != NULL) {
              if (current->data > current->next->data) {
                  return false;
              }
              current = current->next;
          }
}
      return true;
  }

// assuming all equal terms are consecutive
int LinkedList::countDuplicates() const {
	int counter = 0;
	if(front != NULL) {
		LinkedList* current = front;
		while(current->next != NULL) {
			if(current->data == current->next->data) {
				counter++;
			}
			current = current->next;
		}
	}
	return counter;
}

void LinkedList::stutter() {
	// take current data
	// make a copy that points to rest of the list
	LinkedList* cur = front;
	while(cur != NULL) {
		cur->next = new LinkedList(cur->data, cur->next);
		cur = cur->next->next;
	}
}

int LinkedList::deleteBack() {
	if(front == NULL) {
		throw "Empty linked list.";
	}
	LinkedList* current = front;
	while(current != NULL) {
		current = current->next;
	}
	int value = current->data;
	delete current;
	return value;
}

int LinkedList::deleteBack() {
      if (m_front == NULL) {
          throw "empty list";
      }
      int result = 0;
      if (m_front->next == NULL) {
          result = m_front->data;
          delete m_front;
          m_front = NULL;
      } else {
          ListNode* current = m_front;
          while (current->next->next != NULL) {
              current = current->next;
          }
          result = current->next->data;
          delete current->next;
          current->next = NULL;
		}
      return result;
}

void LinkedList::split() {
	LinkedList* cur = front;
	LinkedList* behind = NULL;
	while(cur != NULL) {
		if(cur->data < 0) {
			LinkedList* newCur;
			if(cur->next != NULL) {
				newCur = cur->next;
				behind->next = cur->next;
			} else {
				newCur = NULL;
				behind->next = NULL;
			}
			cur->next = front;
			front = cur;
			cur = newCur;
		} else {
			behind = cur;
			cur = cur->next;
		}
	}
}

void LinkedList::split() {
	if (m_front != NULL) {
		ListNode* current = m_front;
		while (current->next != NULL) {
			if (current->next->data < 0) {
				ListNode* temp = current->next;
				current->next = current->next->next;
				temp->next = m_front;
				m_front = temp;
			} else {
				current = current->next;
			}
		} 
	}
}





 void LinkedList::removeAll(int value) {
    while (m_front != NULL && m_front->data == value) {
        ListNode* trash = m_front;
        m_front = m_front->next;
        delete trash;
    }
    if (m_front != NULL) {
        ListNode* current = m_front;
        while (current->next != NULL) {
            if (current->next->data == value) {
                ListNode* trash = current->next;
                current->next = current->next->next;
                delete trash;
} else {
                current = current->next;
            }
} }
}


  void LinkedList::doubleList() {
      if (m_front != NULL) {
          ListNode* half2 = new ListNode(m_front->data);
          ListNode* back = half2;
          ListNode* current = m_front;
          while (current->next != NULL) {
              current = current->next;
              back->next = new ListNode(current->data);
              back = back->next;
}
          current->next = half2;
      }
}

  void LinkedList::rotate() {
      if (m_front != NULL && m_front->next != NULL) {
          ListNode* temp = m_front;
          m_front = m_front->next;
          ListNode* current = m_front;
          while (current->next != NULL) {
              current = current->next;
          }
          current->next = temp;
          temp->next = NULL;
      }
}


  void LinkedList::reverse() {
      ListNode* current = m_front;
      ListNode* previous = NULL;
      while (current != NULL) {
          ListNode* nextNode = current->next;
          current->next = previous;
          previous = current;
          current = nextNode;
}
      m_front = previous;
  }

