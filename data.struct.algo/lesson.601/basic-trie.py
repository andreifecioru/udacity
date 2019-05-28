#!/usr/bin/env python


class TrieFS:
    def __init__(self):
        self.root = {}

    def mkdir(self, path):
        def _insert(node, items):
            if not items:
                return

            head = items[0]
            tail = items[1:]

            if not head in node:
                node[head] = {}
            _insert(node[head], tail)

        items = [item for item in path.split('/') if item]
        _insert(self.root, items)

    def file(self, path):
        items = [item for item in path.split('/') if item]

        def _walk(node, items):
            if not items:
                return True

            head = items[0]
            tail = items[1:]

            if head not in node:
                return False

            return _walk(node[head], tail)

        return _walk(self.root, items)

    def __repr__(self):
        ret_val = '/'

        def _traverse(node, lvl=1):
            nonlocal ret_val
            for k, v in node.items():
                ret_val += f'\n{"  " * lvl}{k}'
                _traverse(v, lvl + 1)

        _traverse(self.root)
        return ret_val


if __name__ == '__main__':
    root = TrieFS()
    root.mkdir('/usr/local/demdex/keystone/bin/script.sh')
    root.mkdir('/usr/local/demdex/cc-compute/scripts/launch.py')

    print(root)

    # path = '/usr/local/demdex'
    path = '/usr/local/demdex/not_existing'
    if (root.file(path)):
        print(f'Found: {path}')
    else:
        print(f'Not found: {path}')
