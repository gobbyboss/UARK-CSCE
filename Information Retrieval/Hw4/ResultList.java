public class ResultList {
    int docId;
    double weight;
    ResultList next, prev;
    
    public ResultList(int docId, double weight)
    {
        this.docId = docId;
        this.weight = weight;
        this.next = null;
        this.prev = null;
    }

    public int getDocId()
    {
        return this.docId;
    }

    public double getWeight()
    {
        return this.weight;
    }

    public ResultList getNext()
    {
        return this.next;
    }

    public ResultList getPrev()
    {
        return this.prev;
    }
}
