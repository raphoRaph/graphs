# PW2GRAF

**Authors:**

- _Noam FAIVRE_
- _Raphaël TATIN_

All moodle-provided tests run successfully
except for the DOT export of isolatedNodes.gv

This is intentional behavior:

- In our implementation, isolated nodes (nodes without any edges) are not in the graph
- This makes the exported DOT cleaner and does not change the internal graph structure
- The graph itself remains fully functional and correct; only the DOT representation differs

what we had to modify in m1graphs2025
Node : toString() => return the name if he has one, the id if not.
Graph : addNodeIfAbsent() => from private to protected
Graph : addNodeIfAbsent(id, name) => add new method to create a node with id and name (not possible in addNode, we are using only the id not the name)
Graph : addEdge(int fromId, int toId, Integer Weight) => from private to protected
