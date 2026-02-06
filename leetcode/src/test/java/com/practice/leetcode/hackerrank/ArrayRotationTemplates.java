package com.practice.leetcode.hackerrank;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                    ARRAY ROTATION TEMPLATES                                  ║
 * ║                  Templates dễ nhớ cho mọi bài toán                          ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * 3 TEMPLATES QUAN TRỌNG:
 * 1. Left Rotation (xoay trái)
 * 2. Right Rotation (xoay phải)
 * 3. String Rotation Check (kiểm tra rotation)
 */
class ArrayRotationTemplates {

    // ═══════════════════════════════════════════════════════════════════════════
    // TEMPLATE 1: LEFT ROTATION - Xoay Trái k Positions
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * Dễ nhớ: "Reverse 3 lần"
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Example: [1, 2, 3, 4, 5] rotate left by 2
     * 
     * Step 1: Reverse first k elements [0, k)
     * [2, 1, 3, 4, 5]
     *  ↑↑
     * 
     * Step 2: Reverse remaining elements [k, n)
     * [2, 1, 5, 4, 3]
     *        ↑↑↑↑↑
     * 
     * Step 3: Reverse entire array [0, n)
     * [3, 4, 5, 1, 2]  ✓
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Công thức nhớ:
     * 1. Reverse(0, k)
     * 2. Reverse(k, n)
     * 3. Reverse(0, n)
     * 
     * Time: O(n)
     * Space: O(1)
     */
    public void rotateLeft(int[] arr, int k) {
        int n = arr.length;
        k = k % n;  // Handle k > n
        
        // Step 1: Reverse [0, k)
        reverse(arr, 0, k - 1);
        
        // Step 2: Reverse [k, n)
        reverse(arr, k, n - 1);
        
        // Step 3: Reverse [0, n)
        reverse(arr, 0, n - 1);
    }
    
    /**
     * Trace Example:
     * 
     * arr = [1, 2, 3, 4, 5], k = 2
     * 
     * Original: [1, 2, 3, 4, 5]
     * 
     * Step 1: Reverse [0, 2) → reverse first 2 elements
     * [2, 1, 3, 4, 5]
     * 
     * Step 2: Reverse [2, 5) → reverse remaining
     * [2, 1, 5, 4, 3]
     * 
     * Step 3: Reverse [0, 5) → reverse all
     * [3, 4, 5, 1, 2]  ✓
     * 
     * Result: [3, 4, 5, 1, 2]
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TEMPLATE 2: RIGHT ROTATION - Xoay Phải k Positions
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * Dễ nhớ: "Giống Left nhưng đảo ngược thứ tự reverse"
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Example: [1, 2, 3, 4, 5] rotate right by 2
     * 
     * Trick: Right rotate k = Left rotate (n - k)
     * 
     * Method 1: Use rotateLeft
     * rotateRight(arr, k) = rotateLeft(arr, n - k)
     * 
     * Method 2: Reverse 3 lần (thứ tự khác)
     * 
     * Step 1: Reverse entire array [0, n)
     * [5, 4, 3, 2, 1]
     * 
     * Step 2: Reverse first k elements [0, k)
     * [4, 5, 3, 2, 1]
     * 
     * Step 3: Reverse remaining [k, n)
     * [4, 5, 1, 2, 3]  ✓
     * 
     * ─────────────────────────────────────────────────────────────────────────
     * 
     * Công thức nhớ:
     * 1. Reverse(0, n)
     * 2. Reverse(0, k)
     * 3. Reverse(k, n)
     * 
     * Time: O(n)
     * Space: O(1)
     */
    public void rotateRight(int[] arr, int k) {
        int n = arr.length;
        k = k % n;  // Handle k > n
        
        // Step 1: Reverse entire array
        reverse(arr, 0, n - 1);
        
        // Step 2: Reverse first k elements
        reverse(arr, 0, k - 1);
        
        // Step 3: Reverse remaining elements
        reverse(arr, k, n - 1);
    }
    
    /**
     * Hoặc đơn giản hơn:
     */
    public void rotateRightSimple(int[] arr, int k) {
        int n = arr.length;
        k = k % n;
        rotateLeft(arr, n - k);  // Right k = Left (n-k)
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER: Reverse Array [start, end] inclusive
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Two-pointer swap
     * 
     * [1, 2, 3, 4, 5]
     *  ↑           ↑
     *  L           R
     * 
     * Swap → [5, 2, 3, 4, 1]
     *     ↑       ↑
     *     L       R
     * 
     * Swap → [5, 4, 3, 2, 1]
     *        ↑   ↑
     *        L   R
     * 
     * Done when L >= R
     */
    private void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TEMPLATE 3: STRING ROTATION CHECK
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ═══════════════════════════════════════════════════════════════════════════
     * Dễ nhớ: "Concatenation Trick"
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * Kiểm tra s2 có phải rotation của s1?
     * 
     * Key Insight:
     * s2 là rotation của s1 ⇔ s2 là substring của (s1 + s1)
     * 
     * Example:
     * s1 = "abcde"
     * s1 + s1 = "abcdeabcde"
     * 
     * All rotations:
     * - "abcde" (rotate 0)
     * - "bcdea" (rotate 1)
     * - "cdeab" (rotate 2) ← substring at index 2
     * - "deabc" (rotate 3)
     * - "eabcd" (rotate 4)
     * 
     * Time: O(n)
     * Space: O(n)
     */
    public boolean isRotation(String s1, String s2) {
        // Same length?
        if (s1.length() != s2.length()) {
            return false;
        }
        
        // Check if s2 in (s1 + s1)
        return (s1 + s1).contains(s2);
    }
    
    /**
     * Variant: Check NON-IDENTICAL rotation (HackerRank)
     */
    public boolean isNonIdenticalRotation(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return false;
        }
        
        // Must NOT be identical
        if (s1.equals(s2)) {
            return false;
        }
        
        return (s1 + s1).contains(s2);
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TEMPLATE 4: ROTATE STRING (Tạo rotation string)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Tạo string sau khi rotate k positions
     * 
     * Method 1: Substring (Dễ nhớ nhất)
     */
    public String rotateString(String s, int k) {
        int n = s.length();
        k = k % n;
        
        // "abcde" rotate 2 → "cdeab"
        // = s[2:] + s[:2]
        // = "cde" + "ab"
        
        return s.substring(k) + s.substring(0, k);
    }
    
    /**
     * Method 2: Character array (Hiệu quả hơn)
     */
    public String rotateStringArray(String s, int k) {
        char[] arr = s.toCharArray();
        int n = arr.length;
        k = k % n;
        
        // Reverse 3 lần
        reverse(arr, 0, k - 1);
        reverse(arr, k, n - 1);
        reverse(arr, 0, n - 1);
        
        return new String(arr);
    }
    
    private void reverse(char[] arr, int start, int end) {
        while (start < end) {
            char temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // MẸO GHI NHỚ
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * ╔══════════════════════════════════════════════════════════════════════════╗
     * ║                         CHEAT SHEET                                      ║
     * ╚══════════════════════════════════════════════════════════════════════════╝
     * 
     * 1. LEFT ROTATION:
     *    - Reverse(0, k)
     *    - Reverse(k, n)
     *    - Reverse(0, n)
     *    Nhớ: "Chia đôi rồi lật ngược cả hai, cuối cùng lật ngược tất cả"
     * 
     * 2. RIGHT ROTATION:
     *    - Reverse(0, n)
     *    - Reverse(0, k)
     *    - Reverse(k, n)
     *    Nhớ: "Lật ngược tất cả trước, rồi chia đôi lật ngược"
     *    Hoặc: Right k = Left (n-k)
     * 
     * 3. STRING ROTATION CHECK:
     *    - (s1 + s1).contains(s2)
     *    Nhớ: "Nối đôi chứa tất cả"
     * 
     * 4. CREATE ROTATION STRING:
     *    - s.substring(k) + s.substring(0, k)
     *    Nhớ: "Cắt k đem ra sau"
     * 
     * ╔══════════════════════════════════════════════════════════════════════════╗
     * ║                    SO SÁNH LEFT vs RIGHT                                 ║
     * ╚══════════════════════════════════════════════════════════════════════════╝
     * 
     * Input: [1, 2, 3, 4, 5]
     * 
     * LEFT ROTATE by 2:
     * Step 1: [2, 1, 3, 4, 5]  ← Reverse [0, 2)
     * Step 2: [2, 1, 5, 4, 3]  ← Reverse [2, 5)
     * Step 3: [3, 4, 5, 1, 2]  ← Reverse [0, 5)
     * 
     * RIGHT ROTATE by 2:
     * Step 1: [5, 4, 3, 2, 1]  ← Reverse [0, 5)
     * Step 2: [4, 5, 3, 2, 1]  ← Reverse [0, 2)
     * Step 3: [4, 5, 1, 2, 3]  ← Reverse [2, 5)
     * 
     * ╔══════════════════════════════════════════════════════════════════════════╗
     * ║                      KHI NÀO DÙNG GÌ?                                    ║
     * ╚══════════════════════════════════════════════════════════════════════════╝
     * 
     * Array rotation in-place:
     *   → Dùng TEMPLATE 1 hoặc 2 (reverse 3 lần)
     * 
     * Check if rotation:
     *   → Dùng TEMPLATE 3 (s1 + s1).contains(s2)
     * 
     * Create new rotated string:
     *   → Dùng TEMPLATE 4 substring
     * 
     * Need all rotations:
     *   → Loop với TEMPLATE 4
     */
    
    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Test
    @DisplayName("Left Rotation: [1,2,3,4,5] rotate 2 → [3,4,5,1,2]")
    void testLeftRotation() {
        int[] arr = {1, 2, 3, 4, 5};
        rotateLeft(arr, 2);
        assertThat(arr).isEqualTo(new int[]{3, 4, 5, 1, 2});
    }
    
    @Test
    @DisplayName("Right Rotation: [1,2,3,4,5] rotate 2 → [4,5,1,2,3]")
    void testRightRotation() {
        int[] arr = {1, 2, 3, 4, 5};
        rotateRight(arr, 2);
        assertThat(arr).isEqualTo(new int[]{4, 5, 1, 2, 3});
    }
    
    @Test
    @DisplayName("String Rotation Check: abcde, cdeab → true")
    void testStringRotation() {
        assertThat(isRotation("abcde", "cdeab")).isTrue();
        assertThat(isRotation("abcde", "abcde")).isTrue();
        assertThat(isRotation("abcde", "xyz")).isFalse();
    }
    
    @Test
    @DisplayName("Non-Identical Rotation: abcde, cdeab → true")
    void testNonIdenticalRotation() {
        assertThat(isNonIdenticalRotation("abcde", "cdeab")).isTrue();
        assertThat(isNonIdenticalRotation("abcde", "abcde")).isFalse();  // Identical!
    }
    
    @Test
    @DisplayName("Create Rotation String: abcde rotate 2 → cdeab")
    void testRotateString() {
        assertThat(rotateString("abcde", 2)).isEqualTo("cdeab");
        assertThat(rotateStringArray("abcde", 2)).isEqualTo("cdeab");
    }
    
    @Test
    @DisplayName("Edge case: k > n")
    void testLargeK() {
        int[] arr = {1, 2, 3};
        rotateLeft(arr, 5);  // 5 % 3 = 2
        assertThat(arr).isEqualTo(new int[]{3, 1, 2});
    }
    
    @Test
    @DisplayName("Edge case: k = 0")
    void testZeroRotation() {
        int[] arr = {1, 2, 3};
        rotateLeft(arr, 0);
        assertThat(arr).isEqualTo(new int[]{1, 2, 3});
    }
    
    @Test
    @DisplayName("Edge case: Single element")
    void testSingleElement() {
        int[] arr = {1};
        rotateLeft(arr, 5);
        assertThat(arr).isEqualTo(new int[]{1});
    }
}
