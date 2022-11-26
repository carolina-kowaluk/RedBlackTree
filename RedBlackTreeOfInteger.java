import java.util.NoSuchElementException;

public class RedBlackTreeOfInteger {

    private static final class Node {

        public Node father;
        public Node left;
        public Node right;
        public Integer element;
        public boolean color; //vermelho:true, preto:false

        public Node(Integer element, boolean color) {
            this.element = element;
            this.color = color;
            father = nil;
            left = nil;
            right = nil;
        }

    }

    // Atributos
    private Node root;
    private static Node nil;
    private int count;

    public RedBlackTreeOfInteger(){
        count=0;
        nil = new Node(0, false);
        root = nil;
    }

    /**
     * verifica se arvore esta vazia
     * @return true se arvore esta vazia, senão retorna false
     */
    public boolean isEmpty(){
        return (count == 0);
    }

    /**
     * verifica quantos elementos tem na arvore
     * @return numero de elementos na arvore
     */
    public int size(){
        return count;
    }

    /**
     * limpa o conteudo da arvore
     */
    public void clear(){
        root = null;
        count = 0;
    }

    /**
     * verifica qual a altura da arvore
     * @return a altura da arvore
     */
    public int height() {        
        if (root == nil )
            return 0;
        else if (root.left==nil && root.right==nil)
            return 0;
        else
            return height(root);
    }    
    private int height(Node n) {       
        if ( n.left == nil && n.right == nil ) {
            return 0;
        }
        else {
            int h=0;
            if ( n.left != nil )
                h = Math.max(h, height(n.left));
            if ( n.right != nil )
                h = Math.max(h, height(n.right));
            return 1+h;
        }
    } 

    /**
     * verifica se o element esta na arvore
     * @param element
     * @return true se element esta na arvore, senão retorna false
     */
    public boolean contains(Integer element) {
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
    
    /**
     * lista os elementos na arvore no caminhamento pre-fixado
     * @return uma lista usando o caminhamento pre-fixado
     */
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

    /**
     * lista os elementos na arvore no caminhamento pos-fixado
     * @return uma lista usando o caminhamento pos-fixado
     */
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

    /**
     * lista os elementos da arvore no caminhamento central
     * @return uma lista usando o caminhamento central
     */
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

    /**
     * lista os elementos da arvore no caminhamento em largura
     * @return uma lista usando o caminhamento em largura
     */
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
    
    /**
     * retorna o pai do elemento passado por parametro
     * @param element
     * @return o pai do element
     */
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

    /**
     * retorna o avo do nodo passado por parametro
     * @param n nodo
     * @return o avo do nodo
     */
    public Node getAvo(Node n) {
        if (n.father == null) {
            return null; // Nao ter pai significa nao ter avo
        }
        Node pai = n.father;
        return pai.father; // Se o pai tiver pai, retorna-o. Caso contrario, o 
        // retorno do metodo getParent sera null, e o retorno será null.
    }

    /**
     * retorna o tio do nodo passado por parametro
     * @param n nodo
     * @return o tio do nodo
     */
    public Node getTio(Node n) {
        Node avo = getAvo(n);
        if (avo == null) {
            return null;
        }
        Node pai = n.father;
        // Nao existe como saber se o pai eh maior ou menor que o avo, entao
        // para recuperar o tio, comparamos os dois.
        
        if (pai.element.compareTo(avo.element) > 0) { // Se pai > avo
            return avo.left; // retorna o filho da esquerda, que eh menor.
            // Caso nao exista, retorna null.
        } else { // Se pai < avo
            return avo.right; // retorna o filho da direita, que eh maior.
            // Caso nao exista, retorna null.
        }   
    }

    /**
     * faz rotação para esquerda
     * @param n nodo
     */
    public void rotacaoEsquerda(Node n){
       Node aux = n.right;
        n.right = aux.left;
        if(aux.left != null){
            aux.left.father = n;
        }
        aux.father = n.father;
        if(n.father == nil){
            root = aux;
        }
        else if(n == n.father.left){
            n.father.right = aux;
        }
        else{
            n.father.right = aux;
        }
        aux.left = n;
        n.father = aux;
    }

    /**
     * faz rotação para direita
     * @param n
     */
    public void rotacaoDireita(Node n){
        Node aux = n.left;
        n.left = aux.right;
        if(aux.right != nil){
            aux.right.father = n;
        }
        aux.father = n.father;
        if(n.father == null){
            root = aux;
        }
        else if(n == n.father.right){
            n.father.right = aux;
        }
        else {
            n.father.left = aux;
        }
        aux.right = n;
        n.father = aux;
    }

    /**
     * caso 1: o nodo esta na raiz da arvore
     * @param n nodo
     */
    public void fixCase1(Node n) {
        if (n.father == null) { // Se o no for raiz, pinta de preto.
            //n.setColor(Color.BLACK);
            n.color=false;
        } else { // Se nao for, parte para o caso 2.
            fixCase2(n);
        }
    }

    /**
     * caso 2: o pai do nodo é preto
     * @param n nodo
     */
    public void fixCase2(Node n) {
        //Color corDoPai = n.getParent().getColor();
        if (n.father.color==true) { // Se o pai tiver cor vermelha, parte
            fixCase3(n); // para o caso 3. Se nao, a arvore esta correta.
        }
    }

    /**
     * caso 3: o pai e tio são vermelhos e avo é preto, a cor de todos pode ser invertida
     * @param n dodo
     */
    public void fixCase3(Node n) {
        // So se chega a este metodo se o pai do no for vermelho.
        // Verificaremos agora se o tio dele tambem eh vermelho.
        Node tio = getTio(n); // Caso nao exista tio, a variavel recebe null.
        if (tio != null && tio.color==true) {
            // se existe tio e ele eh vermelho:
            // pinta o pai e o tio de preto, o avo de vermelho e roda o fixcase1 no
            // avo para fazer os ajustes, caso estes sejam necessarios.
            Node pai = n.father;
            pai.color = false;
            tio.color = false;
            
            Node avo = getAvo(n);
            avo.color = true;
            fixCase1(avo);
        } else {
            fixCase4(n); // Caso nao haja tio ou ele seja preto, parte ao caso 4.
        }
    }

    /**
     * caso 4: o pai é vermelho mas o tio é preto 
     *  - o nodo é filho a direita e seu pai é filho a esquerda
     * @param n nodo
     */
    public void fixCase4(Node n) {
        Node pai = n.father;
        Node avo = getAvo(n).father;
        
        if (n.element.compareTo(pai.element) > 0 && pai.element.compareTo(avo.element) < 0) {
            // Se o no eh filho da direita e o pai eh filho da esquerda
            rotacaoEsquerda(pai);
            
            n = n.left; // o no passa a ser o seu filho da esquerda, que
            // antes da rotacao era o seu pai.
        } else if (n.element.compareTo(pai.element) < 0 && pai.element.compareTo(avo.element) > 0) {
            // Se o no eh filho da esquerda e o pai eh filho da direita
            rotacaoDireita(pai);
            
            n = n.right; // o no passa a ser o seu filho da direita, que
            // antes da rotacao era o seu pai.
        }
        fixCase5(n);
    }

    /**
     * caso 5: o pai é vermelho mas o tio é preto
     * - o nodo é filho a esquerda e o pai é filho a esquerda
     * @param n
     */
    public void fixCase5(Node n) {
        Node avo = getAvo(n);
        Node tio = getTio(n);
        Node pai = n.father;
        
        // pinta o pai de preto e o avo de vermelho
        avo.color = true;
        pai.color = false;
        
        if (n.element.compareTo(pai.element) < 0 && pai.element.compareTo(avo.element) < 0) {
            // se o no eh filho da esquerda e o pai tambem, 
            // rotaciona o avo a direita.
            rotacaoDireita(avo);
        } else {
            // se nao, quer dizer que o no eh filho da direita e o pai tambem,
            // e rotacionamos o avo a esquerda.
            rotacaoEsquerda(avo);
        }
    }

    public void add(Integer element) {
        add(root, element);
    }

    private Node add(Node root, Integer element){
       return null;
    }
    
    public RedBlackTreeOfInteger clone(){
        RedBlackTreeOfInteger t = new RedBlackTreeOfInteger();
        clone(root, t); 
        return t;
    }
    private Node clone(Node n, RedBlackTreeOfInteger t) {
        Node aux = new Node(n.element, true);
        t.add(aux.element);
        if(n.left!=null){
            clone(n.left, t);
        }
        if(n.right!=null) {
            clone(n.right, t);
        }
        return aux;
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
