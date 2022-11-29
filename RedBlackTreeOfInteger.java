import java.util.NoSuchElementException;

public class RedBlackTreeOfInteger{
    public class Node {
        Integer element;
        Node left;
        Node right;
        Node father;
        boolean color; 
        // VERMELHO: true
        // PRETO: false
        public Node(Integer element, boolean color) {
          this.element = element;
          this.color = color;
          left = nil;
          right = nil;
        }
    }
       
    private int count; //contagem do número de nodos
    private Node root; //referência para o nodo raiz
    private static Node nil; //nodo sentinela

    public RedBlackTreeOfInteger() {
        count = 0;
        nil = new Node(0,false);
        root = nil;
    }

    public void clear() {
        count = 0;
        root = null;
    }

    public boolean isEmpty() {
        return (count == 0);
    }

    public int size() {
        return count;
    }

    public int height() {  //O(n)      
        if (root == nil)
            return 0;
        else if (root.left==nil && root.right==nil)
            return 0;
        else
            return height(root);
    }    
    private int height(Node n) {       
        if ( n.left == nil && n.right == nil) {
            return 0;
        }
        else {
            int h=0;
            if ( n.left != nil)
                h = Math.max(h, height(n.left));
            if ( n.right != nil)
                h = Math.max(h, height(n.right));
            return 1+h;
        }
    } 

    public boolean contains(Integer element) { //O(log n)
        Node n = searchNodeRef(element, root);
        return (n!=null);
    }
    private Node searchNodeRef(Integer element, Node target) {
        if (element == null || target == null)
            return null;
        int c = element.compareTo(target.element);
        if (c == 0)
            return target;
        if (c < 0)
            return searchNodeRef(element, target.left);
        else
            return searchNodeRef(element, target.right);
    }

    //faz rotação para esquerda
    private void rotateLeft(Node n) {
        Node parent = n.father;
        Node direita = n.right;
      
        n.right = direita.left;
        if (direita.left != nil) {
          direita.left.father = n;
        }
      
        direita.left = n;
        n.father = direita;
      
        replaceParentsChild(parent, n, direita);
    }

    //faz rotação para direita
    private void rotateRight(Node n) {
        Node pai = n.father;
        Node esquerda = n.left;
      
        n.left = esquerda.right;
        if (esquerda.right != nil) {
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
      
        if (newChild != nil) {
          newChild.father = father;
        }
    }

    public void add(Integer element) { //O(log n)
        Node n = root;
        Node father = null;
        //enquanto o root não for null/nil
        while (n != nil && n != null) {
          father = n;
          if (element < n.element) {
            n = n.left;
          } else if (element > n.element) {
            n = n.right;
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

    /**
     * garante que as regras da arvore sejam satisfeitas mudando as cores ou chamando os metodos de rotação
     * @param n nodo
     */
    private void fixAdd(Node n) {
        Node parent = n.father;
      
        // CASO 1: o nodo é raiz
        if (parent == null) {
          n.color = false; //raiz tem que ser preta
          return;
        }

        //CASO 2: o nodo pai é vermelho e raiz
        // se o pai for preto a arvore esta ok
        // nesse caso nada precisa ser feito se o caso 1 foi feito
        if (parent.color == false) {
          return;
        }

        Node grandparent = parent.father; //nodo avo
        Node uncle = getUncle(parent); //nodo tio
      
        // CASO 3: pai e tio são vermelhos - recolorir o pai, avo e tio 
        if (uncle != null && uncle.color == true) {
          parent.color = false;
          grandparent.color = true;
          uncle.color = false;
          fixAdd(grandparent);
        }
      
        // CASO 4: pai é vermelho, tio é preto, nodo é neto INTERNO
        // CASO 5: pai é vermelho, tio é preto, nodo é neto EXTERNO
        // a: o pai é filho a ESQUERDA
        else if (parent == grandparent.left) {
          // CASO 4a: tio é preto e nodo é filho a esquerda (neto interno direito)
          if (n == parent.right) {
            rotateLeft(parent); //faz rotação para esquerda
            // pai aponta para o novo nodo da subarvore rotacionada
            parent = n;
          }
        
          // CASO 5a: tio é preto e nodo é filho a esquerda (neto externo esquerdo)
          rotateRight(grandparent); //faz rotação para direita
          // muda a cor do pai e do avo
          parent.color = false;
          grandparent.color = true;
        }
      
        // b: o pai é filho a DIREITA
        else {
          // CASO 4b: tio é preto e nodo é filho a direita (neto interno esquerdo)
          if (n == parent.left) {
            rotateRight(parent); //faz rotação para direita
      
            // pai aponta para o novo nodo da subarvore rotacionada
            parent = n;
          }
      
          // CASO 5b: tio é preto e nodo é filho a direita (neto externo direito)
          rotateLeft(grandparent); //faz rotação para esquerda
          // muda cor do pai e avo 
          parent.color = false;
          grandparent.color = true;
        }
    }

    /**
     * retorna o nodo tio do nodo n
     * @param n nodo
     * @return nodo tio
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

    public Integer getParent(Integer element){
        Node aux = searchNodeRef(element, root);
        if (aux==null) { //se nao encontrou element
            throw new NoSuchElementException();
        }
        if (aux==root) { // se element esta na raiz
            return null;
        }
        return aux.father.element; // retorna o elemento pai
    }

    public RedBlackTreeOfInteger clone(){ //O(n log n)
        RedBlackTreeOfInteger newTree = new RedBlackTreeOfInteger();
        clone(root, newTree); 
        return newTree;
    }
    private Node clone(Node n, RedBlackTreeOfInteger t) {
        Node aux = null;
        if(n.element!=0 && n!=nil){
            aux = new Node(n.element, n.color);
            t.addClone(aux.element, aux.color); //faz o add de uma ABP normal

            if(n.left!=nil && n.left.element!=0){
                clone(n.left, t);
            }
            if(n.right!=nil && n.right.element!=0) {
                clone(n.right, t);
            }
        }
        return aux;
    }
    public void addClone(Integer element, boolean color) {
        Node n = root;
        Node father = null;
        while (n != null && n!=nil) {
          father = n;
          if (element < n.element) {
            n = n.left;
          } else if (element > n.element) {
            n = n.right;
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
        if (n != nil) {
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
        if (n != nil) {
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
        if (n != nil) {
            positionsCentralAux(n.left, res); //Visita a subarvore da esquerda
            res.add(n.element); //Visita o nodo
            positionsCentralAux(n.right, res); //Visita a subarvore da direita
        }
    }

    public LinkedListOfInteger positionsWidth() {
        Queue<Node> fila = new Queue<>();
        Node atual = null;
        LinkedListOfInteger res = new LinkedListOfInteger();
        if (root != nil) {
            fila.enqueue(root);
            while (!fila.isEmpty()) {
                atual = fila.dequeue();
                if (atual.left != nil) {
                    fila.enqueue(atual.left);
                }
                if (atual.right != nil) {
                    fila.enqueue(atual.right);
                }
                res.add(atual.element);
            }
        }
        return res;
    }  

    private void GeraConexoesDOT(Node nodo) {
        if (nodo == nil) {
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
        if (nodo == nil) {
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

    public void GeraDOT() {
        System.out.println("digraph g { \nnode [shape = record,height=.1];\n" + "\n");

        GeraNodosDOT();
        System.out.println("");
        GeraConexoesDOT(root);
        System.out.println("}" + "\n");
    }    
}
