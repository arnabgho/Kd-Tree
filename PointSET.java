/**
 * Auto Generated Java Class.
 */
import java.util.*;
public class PointSET {
    
    TreeSet<Point2D> points;
    int nPoints;
    public PointSET(){
        points=new TreeSet<Point2D>();
        nPoints=0;
    }
        
    public boolean isEmpty(){
        return nPoints==0;
    }
    
    public int size(){
        return nPoints;
    }
    
    public void insert(Point2D p){
        if(points.contains(p))
            return ;
        points.add(p);
        nPoints++;
    }
    
    public boolean contains(Point2D p){
        return points.contains(p);
    }
    
    public void draw(){
      Iterator iterator;
      iterator = points.iterator();
      
      while(iterator.hasNext()){
          Point2D p=(Point2D)iterator.next();
          p.draw();
      }
    }
    
    public Iterable<Point2D> range(RectHV rect){
      Iterator iterator;
      iterator = points.iterator();
      
      Queue<Point2D> q=new Queue<Point2D>();
      while(iterator.hasNext()){
          Point2D p=(Point2D)iterator.next();
         // p.draw();
          if(rect.contains(p))
              q.enqueue(p);
      }
      return q;
    }
    
    public Point2D nearest(Point2D p){
      if(nPoints==0)
          return null;
      Iterator iterator;
      iterator = points.iterator();
      double minDist=1e9;
      Point2D nearPoint=null;
      while(iterator.hasNext()){
          Point2D np=(Point2D)iterator.next();
          double d=np.distanceTo(p);
          if(d<minDist){
              minDist=d;
              nearPoint=np;
          }
      }
      return nearPoint;
    }
    
    public static void main(String[] args){
        double dx[]=new double[]{0.2,0.4,0.3,0.2,0.1};
        double dy[]=new double[]{0.2,0.4,0.3,0.2,0.1};
        PointSET points= new PointSET();
        for(int i=0;i<5;i++){
            Point2D p=new Point2D(dx[i],dy[i]);
            points.insert(p);
        }
        RectHV r=new RectHV(0,0,1,1);
        for(Point2D p : points.range(r)){
            System.out.println("x "+p.x()+" y "+p.y());
           // p.draw();
        }        
        Point2D c=new Point2D(0,0);
        Point2D p=points.nearest(c);
        System.out.println("Nearest x "+p.x()+" y "+p.y());
        points.draw();
    }
}
