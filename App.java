public class App {
    public static void main (String []args){
        //instanciar a arvore
        RedBlackTreeOfInteger b = new RedBlackTreeOfInteger();

        //incluir os números 1,2,3,4,5,6,7,8,9, nesta ordem, na arvore
        b.add(1);
        b.add(2);
        b.add(3);
        b.add(4);
        b.add(5);
        b.add(6);
        b.add(7);
        b.add(8);
        b.add(9);

        //altura da arvore
        System.out.println("altura da arvore: "+b.height());
    
        //chamar o metodo geraDOT() para a arvore
        b.GeraDOT();

        //limpar a arvore
        b.clear();

        //incluir os números 9,8,7,6,5,4,3,2,1, nesta ordem, na arvore
        b.add(9);
        b.add(8);
        b.add(7);
        b.add(6);
        b.add(5);
        b.add(4);
        b.add(3);
        b.add(2);
        b.add(1);

        //caminhamento central
        System.out.println("caminhamento central: ");
        System.out.println(b.positionsCentral());

        //clone da arvore
        RedBlackTreeOfInteger clone = new RedBlackTreeOfInteger();
        clone = b.clone();
       
        //chamar o metodo geraDOT() para o clone
        clone.GeraDOT();
    }
}
