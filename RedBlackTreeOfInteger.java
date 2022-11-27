import java.util.NoSuchElementException;

public class RedBlackTreeOfInteger{
    public class Node {
        Integer element;
        Node left;
        Node right;
        Node father;
        boolean color;
      
        public Node(Integer element, boolean color) {
          this.element = element;
          this.color = color;
        }
    }
       
    private int count; //contagem do número de nodos
    private Node root; //referência para o nodo raiz

    public RedBlackTreeOfInteger() {
        count = 0;
        root = null;
    }

    public void clear() {
        count = 0;
        root = null;
    }

    public boolean isEmpty() {
        return (root == null);
    }

    public int size() {
        return count;
    }

    public int height() {        
        if (root == null)
            return 0;
        else if (root.left==null && root.right==null)
            return 0;
        else
            return height(root);
    }    
    private int height(Node n) {       
        if ( n.left == null && n.right == null) {
            return 0;
        }
        else {
            int h=0;
            if ( n.left != null)
                h = Math.max(h, height(n.left));
            if ( n.right != null)
                h = Math.max(h, height(n.right));
            return 1+h;
        }
    } 

    public Node searchNode(Integer element) {
        Node n = root;
        while (n != null) {
          if (element == n.element) {
            return n;
          } else if (element < n.element) {
            n = n.left;
          } else {
            n = n.right;
          }
        }
        return null;
      }

    /**
     * faz rotação para a esquerda
     * @param n nodo
     */
    private void rotateLeft(Node n) {
        Node parent = n.father;
        Node direita = n.right;
      
        n.right = direita.left;
        if (direita.left != null) {
          direita.left.father = n;
        }
      
        direita.left = n;
        n.father = direita;
      
        replaceParentsChild(parent, n, direita);
    }

    /**
     * faz rotação para a direita
     * @param n nodo
     */
    private void rotateRight(Node n) {
        Node pai = n.father;
        Node esquerda = n.left;
      
        n.left = esquerda.right;
        if (esquerda.right != null) {
          esquerda.right.father = n;
        }
      
        esquerda.right = n;
        n.father = esquerda;
      
        replaceParentsChild(pai, n, esquerda);
    }
    
    /**
     * define o relacionamento pai-filho entre o pai do antigo nodo 
     * da subarvore em rotação e o novo nodo 
     * @param father
     * @param old
     * @param newChild
     */
    private void replaceParentsChild(Node father, Node old, Node newChild) {
        if (father == null) {
          root = newChild;
        } else if (father.left == old) {
          father.left = newChild;
        } else if (father.right == old) {
          father.right = newChild;
        } else {
          throw new IllegalStateException("O nodo não é filho do nodo pai");
        }
      
        if (newChild != null) {
          newChild.father = father;
        }
    }

    /**
     * adiciona elemento na lista
     * @param element
     */
    public void add(Integer element) {
        Node n = root;
        Node father = null;
      
        //enquanto o root não for null
        while (n != null) {
          father = n;
          if (element < n.element) {
            n = n.left;
          } else if (element > n.element) {
            n = n.right;
          } else {
            throw new IllegalArgumentException("A árvore já contém o elemento " + element);
          }
        }
        //insere novo nodo
        Node newNode = new Node(element, true);
        if (father == null) {
          root = newNode;
        } else if (element < father.element) {
          father.left = newNode;
        } else {
          father.right = newNode;
        }
        newNode.father = father;
        fixAdd(newNode);
    }

    private void fixAdd(Node node) {
        Node parent = node.father;
      
        // Case 1: o nodo é raiz, o pai é null
        if (parent == null) {
          // Uncomment the following line if you want to enforce black roots (rule 2):
          node.color = false;
          return;
        }
      
        // Parent is black --> nothing to do
        if (parent.color == false) {
          return;
        }
      
        // From here on, parent is red
        Node grandparent = parent.father;
      
        // Case 2:
        // Not having a grandparent means that parent is the root. If we enforce black roots
        // (rule 2), grandparent will never be null, and the following if-then block can be
        // removed.
        if (grandparent == null) {
          // As this method is only called on red nodes (either on newly inserted ones - or -
          // recursively on red grandparents), all we have to do is to recolor the root black.
          parent.color = false;
          return;
        }
      
        // Get the uncle (may be null/nil, in which case its color is BLACK)
        Node uncle = getUncle(parent);
      
        // Case 3: Uncle is red -> recolor parent, grandparent and uncle
        if (uncle != null && uncle.color == true) {
          parent.color = false;
          grandparent.color = true;
          uncle.color = false;
      
          // Call recursively for grandparent, which is now red.
          // It might be root or have a red parent, in which case we need to fix more...
          fixAdd(grandparent);
        }
      
        // Parent is left child of grandparent
        else if (parent == grandparent.left) {
          // Case 4a: Uncle is black and node is left->right "inner child" of its grandparent
          if (node == parent.right) {
            rotateLeft(parent);
      
            // Let "parent" point to the new root node of the rotated sub-tree.
            // It will be recolored in the next step, which we're going to fall-through to.
            parent = node;
          }
      
          // Case 5a: Uncle is black and node is left->left "outer child" of its grandparent
          rotateRight(grandparent);
      
          // Recolor original parent and grandparent
          parent.color = false;
          grandparent.color = true;
        }
      
        // Parent is right child of grandparent
        else {
          // Case 4b: Uncle is black and node is right->left "inner child" of its grandparent
          if (node == parent.left) {
            rotateRight(parent);
      
            // Let "parent" point to the new root node of the rotated sub-tree.
            // It will be recolored in the next step, which we're going to fall-through to.
            parent = node;
          }
      
          // Case 5b: Uncle is black and node is right->right "outer child" of its grandparent
          rotateLeft(grandparent);
      
          // Recolor original parent and grandparent
          parent.color = false;
          grandparent.color = true;
        }
    }

    /**
     * acha tio do nodo
     * @param n
     * @return o nodo tio
     */
    private Node getUncle(Node n) {
        Node grandparent = n.father;
        if (grandparent.left == n) {
          return grandparent.right;
        } else if (grandparent.right == n) {
          return grandparent.left;
        } else {
          throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    public boolean getColor(Integer element){
        Node aux = searchNode(element);
        if (aux==null) { //se nao encontrou element
            throw new NoSuchElementException();
        }
        return aux.color;
    }

    public Integer getLeft(Integer element) {
        Node aux = searchNode(element);
        if(aux==null)
            throw new NoSuchElementException();
        if(aux.left==null)
            return null;
        return aux.left.element;
    }

    public Integer getRight(Integer element) {
        Node aux = searchNode(element);
        if(aux==null)
            throw new NoSuchElementException();
        if(aux.right==null)
            return null;
        return aux.right.element;
    }

    public Integer getParent(Integer element){
        Node aux = searchNode(element);
        if (aux==null) { //se nao encontrou element
            throw new NoSuchElementException();
        }
        if (aux==root) { // se element esta na raiz
            return null;
        }
        return aux.father.element; // retorna o elemento pai
    }

    public RedBlackTreeOfInteger clone(){
        RedBlackTreeOfInteger newTree = new RedBlackTreeOfInteger();
        clone(root, newTree); 
        return newTree;
    }
    private Node clone(Node n, RedBlackTreeOfInteger t) {
        Node aux = new Node(n.element, n.color);
        t.addClone(aux.element, aux.color);
        if(n.left!=null){
            clone(n.left, t);
        }
        if(n.right!=null) {
            clone(n.right, t);
        }
        return aux;
    }
    private void addClone(Integer element, boolean color){
        Node n = root;
        Node father = null;

        while (n != null) {
          father = n;
          if (element < n.element) {
            n = n.left;
          } else if (element > n.element) {
            n = n.right;
          } else {
            throw new IllegalArgumentException("BST already contains a node with key " + element);
          }
        }
      
        Node newNode = new Node(element, color);
        if (father == null) {
          root = newNode;
        } else if (element < father.element) {
          father.left = newNode;
        } else {
          father.right = newNode;
        }
        newNode.father = father;
    }

    public LinkedListOfInteger positionsPre() {
        LinkedListOfInteger res = new LinkedListOfInteger();
        positionsPreAux(root, res);
        return res;
    }
    private void positionsPreAux(Node n, LinkedListOfInteger res) {
        if (n != null) {
            res.add(n.element); //Visita o nodo
            positionsPreAux(n.left, res); //Visita a subarvore da esquerda
            positionsPreAux(n.right, res); //Visita a subarvore da direita
        }

    }

    public LinkedListOfInteger positionsPos() {
        LinkedListOfInteger res = new LinkedListOfInteger();
        positionsPosAux(root, res);
        return res;
    }
    private void positionsPosAux(Node n, LinkedListOfInteger res) {
        if (n != null) {
            positionsPosAux(n.left, res); //Visita a subarvore da esquerda
            positionsPosAux(n.right, res); //Visita a subarvore da direita
            res.add(n.element); //Visita o nodo
        }
    }

    public LinkedListOfInteger positionsCentral() {
        LinkedListOfInteger res = new LinkedListOfInteger();
        positionsCentralAux(root, res);
        return res;
    }
    private void positionsCentralAux(Node n, LinkedListOfInteger res) {
        if (n != null) {
            positionsCentralAux(n.left, res); //Visita a subarvore da esquerda
            res.add(n.element); //Visita o nodo
            positionsCentralAux(n.right, res); //Visita a subarvore da direita
        }
    }

    public LinkedListOfInteger positionsWidth() {
        Queue<Node> fila = new Queue<>();
        Node atual = null;
        LinkedListOfInteger res = new LinkedListOfInteger();
        if (root != null) {
            fila.enqueue(root);
            while (!fila.isEmpty()) {
                atual = fila.dequeue();
                if (atual.left != null) {
                    fila.enqueue(atual.left);
                }
                if (atual.right != null) {
                    fila.enqueue(atual.right);
                }
                res.add(atual.element);
            }
        }
        return res;
    }  

    private void GeraConexoesDOT(Node nodo) {
        if (nodo == null) {
            return;
        }

        GeraConexoesDOT(nodo.left);
        //   "nodeA":esq -> "nodeB" [color="0.650 0.700 0.700"]
        if (nodo.left != null) {
            System.out.println("\"node" + nodo.element + "\":esq -> \"node" + nodo.left.element + "\" " + "\n");
        }

        GeraConexoesDOT(nodo.right);
        //   "nodeA":dir -> "nodeB";
        if (nodo.right != null) {
            System.out.println("\"node" + nodo.element + "\":dir -> \"node" + nodo.right.element + "\" " + "\n");
        }
        //"[label = " << nodo->hDir << "]" <<endl;
    }

    private void GeraNodosDOT(Node nodo) {
        if (nodo == null) {
            return;
        }
        GeraNodosDOT(nodo.left);
        //node10[label = "<esq> | 10 | <dir> "];
        System.out.println("node" + nodo.element + "[label = \"<esq> | " + nodo.element + " | <dir> \"]" + "\n");
        GeraNodosDOT(nodo.right);
    }

    public void GeraConexoesDOT() {
        GeraConexoesDOT(root);
    }

    public void GeraNodosDOT() {
        GeraNodosDOT(root);
    }

    // Gera uma saida no formato DOT
    // Esta saida pode ser visualizada no GraphViz
    // Versoes online do GraphViz pode ser encontradas em
    // http://www.webgraphviz.com/
    // http://viz-js.com/
    // https://dreampuf.github.io/GraphvizOnline 
    public void GeraDOT() {
        System.out.println("digraph g { \nnode [shape = record,height=.1];\n" + "\n");

        GeraNodosDOT();
        System.out.println("");
        GeraConexoesDOT(root);
        System.out.println("}" + "\n");
    }    
}
