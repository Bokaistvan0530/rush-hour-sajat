package hu.rushhour.model;
import hu.rushhour.model.*;
import hu.rushhour.io.*;
import hu.rushhour.db.*;
import hu.rushhour.game.*;



import java.util.*;
/** A játéktábla szabályai; a felület és az adatbázis nem része. */
public final class Board {
 private final int rows, cols;
 private final Map<Character,Vehicle> vehicles=new LinkedHashMap<>();
 public Board(int rows,int cols){if(rows<2||rows>99||cols<4||cols>26)throw new IllegalArgumentException("Invalid board size");this.rows=rows;this.cols=cols;}
 public int rows(){return rows;} public int cols(){return cols;}
 public Collection<Vehicle> vehicles(){return List.copyOf(vehicles.values());}
 public Vehicle get(char id){return vehicles.get(Character.toUpperCase(id));}
 public void add(Vehicle v){if(v==null||vehicles.containsKey(v.id())||!fits(v,v.id()))throw new IllegalArgumentException("Invalid placement: "+v.id());vehicles.put(v.id(),v);}
 private boolean fits(Vehicle v,char ignore){for(int i=0;i<v.length();i++){
  int r=v.row()+(v.horizontal()?0:i),c=v.col()+(v.horizontal()?i:0);
  if(r<0||r>=rows||c<0||c>=cols)return false;
  for(Vehicle other:vehicles.values())if(other.id()!=ignore)for(int j=0;j<other.length();j++)
   if(r==other.row()+(other.horizontal()?0:j)&&c==other.col()+(other.horizontal()?j:0))return false;
 }return true;}

 public boolean canMove(char id, int delta) {
  Vehicle v = get(id);

  if (v == null || delta == 0) {
   return false;
  }

  for (int n = 1; n <= Math.abs(delta); n++) {
   int step = Integer.signum(delta) * n;

   int newRow = v.row() + (v.horizontal() ? 0 : step);
   int newCol = v.col() + (v.horizontal() ? step : 0);

   if (newRow < 0 || newCol < 0) {
    return false;
   }

   Vehicle shifted = v.shifted(step);

   if (!fits(shifted, v.id())) {
    return false;
   }
  }

  return true;
 }

 public void move(char id,int delta){if(!canMove(id,delta))throw new IllegalArgumentException("Illegal move");Vehicle v=get(id);vehicles.put(v.id(),v.shifted(delta));}
 public List<Move> moves(){List<Move> result=new ArrayList<>();for(Vehicle v:vehicles.values())for(int d=-Math.max(rows,cols);d<=Math.max(rows,cols);d++)if(canMove(v.id(),d))result.add(new Move(v.id(),d));return result;}
 /** Kijárat: A jobbra, B balra; csak a kijáratnál és szabad úton nyerhet. */
 public boolean canExit(char id){id=Character.toUpperCase(id);Vehicle v=get(id);if(v==null||!v.horizontal())return false;
  return id=='A'?v.col()+v.length()==cols:id=='B'&&v.col()==0;}
 public boolean exit(char id){id=Character.toUpperCase(id);if(!canExit(id))return false;vehicles.remove(id);return true;}
 public void validateHeroes(){Vehicle a=get('A'),b=get('B');if(a==null||b==null||!a.horizontal()||!b.horizontal())throw new IllegalArgumentException("A and B must be horizontal hero cars");}
 public String render(){char[][] cells=new char[rows][cols];for(char[] row:cells)Arrays.fill(row,'.');
  for(Vehicle v:vehicles.values())for(int i=0;i<v.length();i++)cells[v.row()+(v.horizontal()?0:i)][v.col()+(v.horizontal()?i:0)]=v.id();
  StringBuilder out=new StringBuilder("   ");for(int c=0;c<cols;c++)out.append((char)('a'+c)).append(' ');out.append('\n');
  for(int r=0;r<rows;r++){out.append(String.format("%2d ",r+1));for(char ch:cells[r])out.append(ch).append(' ');out.append('\n');}return out.toString();}
 public record Move(char id,int delta){}
}
