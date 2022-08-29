struct quadtree {
	int lowx, highx; // smallest and largest x value covered by node
	int lowy, highy; // smallest and largest y value covered by node
	bool isBlack; // entirely black? true. Entirely white? False. Mixed? ignored 
	quadtree *children[4]; // 0 is NW, 1 is NE, 2 is SE, 3 is SW
};

static quadtree gridToQuadTree(Grid<bool>& image) {

}
