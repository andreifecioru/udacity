from operator import attrgetter

class Path:
    def __init__(self, intersection, distance, estimate, path):
        self.intersection = intersection
        self.distance = distance
        self.estimate = estimate
        self.path = path
        self.cost = self.distance + self.estimate
        
    def __repr__(self):
        return f"Path({self.intersection}, cost: {self.cost}, path: {self.path})"
        
    
class PathFinder:
    def __init__(self, _map, start, goal):
        self.map = _map
        self.start = start
        self.goal = goal
        
        self.explored = {self.start}
        self.current = Path(
            intersection=self.start, 
            distance=0, 
            estimate=self._compute_distance(self.start, self.goal), 
            path=[self.start])
        self.frontier = {}
        
        self.solution = None
        
    def compute_shortest_path(self):
        while not self._explore():  # keep exploring until done
            # print(f"Current intersection: {self.current}")
            pass
            
        return self.solution.path if self.solution else None
        
    def _explore(self):
        # update the frontier by expanding from the current intersection
        self._update_frontier()
        
        # update the current intersection
        self._update_current()
        
        # goal check
        self._check_goal()
        
        # remove the current intersection from the frontier
        # (it is now marked as explored)
        del self.frontier[self.current.intersection]
        
        # we're not done while there are still nodes left to be explored on the frontier
        return not self.frontier
        
    def _update_frontier(self):
        # if we haven't reached our goal yet
        if self.current.intersection != self.goal:
            # we expand the frontier from the current intersection
            for intersection in self.map.roads[self.current.intersection]:
                # ignore the intersections that have already been explored
                if intersection in self.explored:
                    continue
                    
                new_frontier_intersection = Path(
                    intersection=intersection,
                    distance=self.current.distance + self._compute_distance(self.current.intersection, intersection),
                    estimate=self._compute_distance(self.goal, intersection),
                    path=self.current.path[:] + [intersection])
                
                # we place the intersection on the frontier if:
                #  - the intersection was not on the frontier to begin with
                #  - the intersection was previously on the frontier, by we found a better path (lower cost)
                if intersection not in self.frontier or \
                   new_frontier_intersection.distance < self.frontier[intersection].distance:
                    self.frontier[intersection] = new_frontier_intersection
                    
    def _update_current(self):
        # add the current intersection to the set of explored ones
        self.explored.add(self.current.intersection)
        
        # choose the intersection with the lowest cost to be
        # the next "current intersection"
        self.current = min(self.frontier.values(), key=attrgetter('cost'))
        
    def _check_goal(self):
        if self.current.intersection == self.goal:
            # we have reached our goal: update the solution 
            # (if it's better than what we've found so far)
            if not self.solution or self.current.cost < self.solution.cost:
                self.solution = self.current
            
                    
    def _compute_distance(self, intersection_1, intersection_2):
        p1 = self.map.intersections[intersection_1]
        p2 = self.map.intersections[intersection_2]
        return (abs(p1[0] - p2[0])**2 + abs(p1[1] - p2[1])**2)**.5
                
        

def shortest_path(M, start, goal):
    pf = PathFinder(M, start, goal)
    return pf.compute_shortest_path()