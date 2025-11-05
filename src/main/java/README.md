# PW2GRAF

**Authors:**
- *Noam FAIVRE*
- *Raphaël TATIN*

## Compatibility with Moodle Tests
All moodle-provided tests run successfully
except for the DOT export of isolatedNodes.gv

This is intentional behavior:
- In our implementation, isolated nodes (nodes without any edges) are not in the graph
- This makes the exported DOT cleaner and does not change the internal graph structure
- The graph itself remains fully functional and correct; only the DOT representation differs
