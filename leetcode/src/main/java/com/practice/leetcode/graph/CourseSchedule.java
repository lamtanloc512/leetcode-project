package com.practice.leetcode.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Course Schedule
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/course-schedule/
 *
 * Description:
 * There are a total of numCourses courses you have to take, labeled from 0
 * to numCourses - 1. You are given an array prerequisites where
 * prerequisites[i] = [ai, bi] indicates that you must take course bi first
 * if you want to take course ai.
 * 
 * For example, the pair [0, 1], indicates that to take course 0 you have to
 * first take course 1.
 * 
 * Return true if you can finish all courses. Otherwise, return false.
 *
 * Examples:
 * Input: numCourses = 2, prerequisites = [[1,0]]
 * Output: true
 * Explanation: There are a total of 2 courses to take. To take course 1 you
 * should have finished course 0. So it is possible.
 *
 * Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
 * Output: false
 * Explanation: There are a total of 2 courses to take. To take course 1 you
 * should have finished course 0, and to take course 0 you should also have
 * finished course 1. So it is impossible.
 *
 * Constraints:
 * - 1 <= numCourses <= 2000
 * - 0 <= prerequisites.length <= 5000
 * - prerequisites[i].length == 2
 * - 0 <= ai, bi < numCourses
 * - All the pairs prerequisites[i] are unique.
 */
public class CourseSchedule {

  /**
   * Determine if all courses can be finished.
   *
   * @param numCourses    total number of courses
   * @param prerequisites prerequisite pairs
   * @return true if all courses can be finished, false otherwise
   */
  public boolean canFinish(int numCourses, int[][] prerequisites) {
    // Build adjacency list: course -> list of courses that depend on it
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) {
      graph.add(new ArrayList<>());
    }
    
    for (int[] prereq : prerequisites) {
      // prereq[1] must be taken before prereq[0]
      graph.get(prereq[1]).add(prereq[0]);
    }
    
    // State: 0 = unvisited, 1 = visiting (in current path), 2 = visited (processed)
    int[] state = new int[numCourses];
    
    // Check each course for cycles
    for (int i = 0; i < numCourses; i++) {
      if (hasCycle(graph, i, state)) {
        return false;
      }
    }
    
    return true;
  }
  
  private boolean hasCycle(List<List<Integer>> graph, int course, int[] state) {
    if (state[course] == 1) return true;  // Found cycle - visiting a node in current path
    if (state[course] == 2) return false; // Already fully processed
    
    state[course] = 1; // Mark as visiting
    
    for (int next : graph.get(course)) {
      if (hasCycle(graph, next, state)) {
        return true;
      }
    }
    
    state[course] = 2; // Mark as visited (fully processed)
    return false;
  }
}
