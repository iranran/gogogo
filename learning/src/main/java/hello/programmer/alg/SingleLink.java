package hello.programmer.alg;

public class SingleLink {

    private Node first;

    private Node last;

    public Node add(Node node){
        if (first == null) {
            first = node;
            last = node;
        }
        else {
            last.next = node;
            last = node;
        }
        return first;
    }



    public static void main(String[] args) {
        SingleLink link = new SingleLink();
        Node first = null;
        for (int i = 0; i< 10; i++) {
            Node node = new Node(i);
            first = link.add(node);
        }
        Node temp = first;
        while(temp != null){
            System.out.println(temp.value);
            temp = temp.next;
        }
    }



}
