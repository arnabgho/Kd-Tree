class Node{
    Point2D p;         // the point
    RectHV rect;       // the axis-aligned rectangle corresponding to this node
    Node lb;           // Left or Bottom
    Node rt;           // Right or Top
    public Node(Point2D point){
        p=point;
        lb=null;
        rt=null;
        rect=null;
    }
}


public class KdTree {
    
    int nNodes;
    Node Root;
    public KdTree(){
        nNodes=0;
        Root=null;
    }                               
    public boolean isEmpty(){
        return nNodes==0;
    }                      
    public int size(){
        return nNodes;
    }                         
    public void insert(Point2D p){
        Node In=new Node(p);
        if(Root==null){
            RectHV r=new RectHV(0,0,1,1);
            In.rect=r;
            Root=In;
            nNodes++;
            return ;
        }
        if(this.contains(p))
            return ;
        nNodes++;
        int level=0;
        double x0,x1,y0,y1;
        Node n=Root;
        Boolean inserted=false;
        while(!inserted){
            x0=n.rect.xmin();
            y0=n.rect.ymin();
            x1=n.rect.xmax();
            y1=n.rect.ymax();
            if(level%2==0){                  //X-coordinate
                if(p.x()<n.p.x()){
                    if(n.lb!=null)
                        n=n.lb;
                    else{
                        RectHV r= new RectHV(x0,y0,n.p.x(),y1);                                                     //new RectHV(x0,y0,p.x(),y1);
                        In.rect=r;
                        n.lb=In;
                        inserted=true;
                        break;
                    }
                }    
                else{
                    if(n.rt!=null)
                        n=n.rt;
                    else{
                        RectHV r=   new RectHV(n.p.x(),y0,x1,y1);                                                   //new RectHV(p.x(),y0,x1,y1);
                        In.rect=r;
                        n.rt=In;
                        inserted=true;
                        break;
                    }
                }    
            }
            else{                            //Y-coordinate
                if(p.y()<n.p.y()){
                    if(n.lb!=null)
                        n=n.lb;
                    else{
                        RectHV r= new RectHV(x0,y0,x1,n.p.y());                         //new RectHV(x0,y0,x1,p.y());
                        In.rect=r;
                        n.lb=In;
                        inserted=true;
                        break;
                    }
                }    
                else{
                   if(n.rt!=null)
                        n=n.rt;
                    else{
                        RectHV r= new RectHV(x0,n.p.y(),x1,y1);                        //new RectHV(x0,p.y(),x1,y1);
                        In.rect=r;
                        n.rt=In;
                        inserted=true;
                        break;
                    }
                }    
            }
            level++;
        }
    }              
    public boolean contains(Point2D p){
        if(Root==null)
            return false;
        int level=0;
        Node n=Root;
        while(n!=null){
            if(n.p.x()==p.x() && n.p.y()==p.y())
                return true;
            if(level%2==0){                  //X-coordinate
                if(p.x()<n.p.x())
                    n=n.lb;
                else
                    n=n.rt;
            }
            else{                            //Y-coordinate
                if(p.y()<n.p.y())
                    n=n.lb;
                else
                    n=n.rt;
            }
            level++;
        }
        return false;
    }            
    public void recursiveDraw(Node n,int level,double x0,double y0,double x1,double y1){
        if(n==null)
            return ;
        if(level%2==0){
            double x=n.p.x();
            double y=n.p.y();
            
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            
            StdDraw.point(x,y);    
 
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();     
            
            StdDraw.line(x,y0,x,y1);
            recursiveDraw(n.lb,level+1,x0,y0,x,y1);
            recursiveDraw(n.rt,level+1,x,y0,x1,y1);
        }
        else{
            double x=n.p.x();
            double y=n.p.y();
            
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            
            StdDraw.point(x,y);    
 
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            
            StdDraw.line(x0,y,x1,y);
            recursiveDraw(n.lb,level+1,x0,y0,x1,y);
            recursiveDraw(n.rt,level+1,x0,y,x1,y1);
        }
    }
    
    public void draw(){
        StdDraw.rectangle(0.5,0.5,0.5,0.5);
        recursiveDraw(Root,0,0,0,1,1);
    }   
    
    public Iterable<Point2D> recursiveRange(Node n,RectHV rect){
        if(n==null)
            return null;
        Queue<Point2D> q=new Queue<Point2D>();
        if(rect.contains(n.p))
            q.enqueue(n.p);
        if(n.lb!=null && n.lb.rect.intersects(rect)){
            for(Point2D p :recursiveRange(n.lb,rect)){
                q.enqueue(p);
            }
        }
        if(n.rt!=null && n.rt.rect.intersects(rect)){
            for(Point2D p :recursiveRange(n.rt,rect)){
                q.enqueue(p);
            }
        }
        return q;
    }
    
    public Iterable<Point2D> range(RectHV rect){
        return recursiveRange(Root,rect);    
    }
    
    public Point2D nearest(Point2D p){
        if(nNodes==0)
            return null;
        Queue<Node> q=new Queue<Node>();
        Node n=Root;
        Point2D bestPoint=n.p;
        double best=p.distanceTo(n.p);
        q.enqueue(n);
        while(!q.isEmpty()){
            Node node=q.dequeue();
            if(node==null) continue;
            if(node.rect.distanceTo(p)>best) continue;
            if(p.distanceTo(node.p)<best){
                best=p.distanceTo(node.p);
                bestPoint=node.p;
            }
            q.enqueue(node.lb);
            q.enqueue(node.rt);
        }
        return bestPoint;
    }             

    public static void main(String[] args){
        String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        // initialize the two data structures with point from standard input
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            System.out.println("x "+x+" y "+y );
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }
        kdtree.draw();
        StdDraw.show(50);
    }        
}