// src/pages/game/components/in-game/constants/test-problem.js

/**
 * 인게임 문제 테스트 데이터
 * 피그마 디자인에서 추출한 실제 문제 내용
 */
export const testProblem = {
  id: 1,
  title: '이진 트리의 최대 깊이 구하기',
  difficulty: 'Medium',
  category: 'Tree',

  description:
    '이진 트리가 주어졌을 때, 트리의 최대 깊이를 구하는 함수를 작성하세요. 트리의 깊이는 루트 노드에서 가장 깊은 리프 노드까지의 경로에 있는 노드의 개수입니다.',

  examples: [
    {
      input: 'root = [3,9,20,null,null,15,7]',
      output: '3',
      explanation: '최대 깊이는 3입니다 (3 -> 20 -> 7)',
    },
    {
      input: 'root = [1,null,2]',
      output: '2',
      explanation: '최대 깊이는 2입니다 (1 -> 2)',
    },
  ],

  constraints: ['트리의 노드 개수는 [0, 10^4] 범위입니다', '노드의 값은 [-100, 100] 범위입니다', '시간 복잡도: O(n)'],

  // 초기 코드 템플릿
  starterCode: `# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def maxDepth(self, root: TreeNode) -> int:
        """
        이진 트리의 최대 깊이를 구하는 함수

        Args:
            root: 이진 트리의 루트 노드

        Returns:
            int: 트리의 최대 깊이
        """
        # 여기에 코드를 작성하세요
        pass`,

  // 테스트 케이스
  testCases: [
    {
      input: '[3,9,20,null,null,15,7]',
      expected: '3',
    },
    {
      input: '[1,null,2]',
      expected: '2',
    },
    {
      input: '[]',
      expected: '0',
    },
  ],
};
