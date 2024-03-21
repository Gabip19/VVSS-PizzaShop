package pizzashop.repository;

import pizzashop.model.MenuItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MenuRepository {
    private static final String FILE_NAME = "data/menu.txt";
    private List<MenuItem> listMenu;

    public MenuRepository(){
        readMenu();
    }

    private void readMenu(){
        //ClassLoader classLoader = MenuRepository.class.getClassLoader();
        File file = new File(FILE_NAME);
        this.listMenu= new ArrayList();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line = null;
            while((line=br.readLine())!=null){
                MenuItem menuItem=getMenuItem(line);
                listMenu.add(menuItem);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MenuItem getMenuItem(String line){
        MenuItem item=null;
        if (line==null|| line.equals("")) return null;
        StringTokenizer st=new StringTokenizer(line, ",");
        String name= st.nextToken();
        double price = Double.parseDouble(st.nextToken());
        item = new MenuItem(name, 0, price);
        return item;
    }

    public List<MenuItem> getMenu(){
        return listMenu;
    }
}
