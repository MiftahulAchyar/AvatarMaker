package com.dandelionteam.avatarmaker;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class CreateAvatar extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private HorizontalListView kategori, subkategori;
    private HLVAdapter kategoriAdapter, subkategoriAdapter;

    Integer[] gbrKategori={R.mipmap.wajah1, R.mipmap.mata1, R.mipmap.mulut1};
    Integer[][] gbrSubkategori={
            {R.mipmap.wajah1, R.mipmap.wajah2},
            {R.mipmap.mata1, R.mipmap.mata2},
            {R.mipmap.mulut1, R.mipmap.mulut2}
    };

//    String[] namaKategori={"Wajah", "Mata", "Mulut"};   String[] namaSubkategori;
    int selectedKat=0, selectedSubkat[];

    //ArrayList<String> kategoriName, subkategoriName;
    ArrayList<Integer> kategoriImage, subkategoriImage;

    ImageView[] imageViews = new ImageView[gbrKategori.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        selectedSubkat = new int[gbrKategori.length];
        for (int i=0; i<selectedSubkat.length;i++)
            selectedSubkat[i]=0;

        imageViews[0] = (ImageView)findViewById(R.id.wajah);
        imageViews[1] = (ImageView)findViewById(R.id.mata);
        imageViews[2] = (ImageView)findViewById(R.id.mulut);

        imageViews[0].setImageResource(gbrSubkategori[0][0]);
        imageViews[1].setImageResource(gbrSubkategori[1][0]);
        imageViews[2].setImageResource(gbrSubkategori[2][0]);

        setKategori();
        kategori.setSelection(0);
        setSubkategori(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getApplication()).inflate(R.menu.menu1_save,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.save){simpanGambar();}
        return super.onOptionsItemSelected(item);
    }

    void setSubkategori(int postion){
        selectedKat=postion;
        String[] nama = new String[gbrSubkategori[postion].length];
        for (int i=0; i<gbrSubkategori[postion].length; i++)
            nama[i] = "";
//        subkategoriName = new ArrayList<>(Arrays.asList(nama));
        subkategoriImage = new ArrayList<>(Arrays.asList(gbrSubkategori[0]));

        subkategori=(HorizontalListView) findViewById(R.id.subkategori);

        subkategoriAdapter = new HLVAdapter(CreateAvatar.this,  subkategoriImage);
        subkategori.setAdapter(subkategoriAdapter);
        subkategori.setOnItemClickListener(this);
    }
    void setKategori(){
//        kategoriName = new ArrayList<>(Arrays.asList(namaKategori));
//        Take your won images for your app and give drawable path as below to arraylist
        kategoriImage = new ArrayList<>(Arrays.asList(gbrKategori));
        kategori = (HorizontalListView) findViewById(R.id.kategori);

        kategoriAdapter = new HLVAdapter(CreateAvatar.this,  kategoriImage);
        kategori.setAdapter(kategoriAdapter);

        kategori.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
        switch (adapterView.getId()){
            case R.id.kategori:
                //Toast.makeText(CreateAvatar.this, "You clicked on : " + kategoriImage.get(postion).toString(), Toast.LENGTH_LONG).show();
                selectedKat=postion;
                String[] nama = new String[gbrSubkategori[postion].length];
                for (int i=0; i<gbrSubkategori[postion].length; i++) {
                    nama[i] = i + "";
                    Log.e("NamaSub",""+nama[i]);
                }
                //subkategoriName = new ArrayList<>(Arrays.asList(nama));
                subkategoriImage = new ArrayList<>(Arrays.asList(gbrSubkategori[postion]));

                subkategori=(HorizontalListView) findViewById(R.id.subkategori);

                subkategoriAdapter = new HLVAdapter(CreateAvatar.this, subkategoriImage);

                subkategori.setAdapter(subkategoriAdapter);

                subkategori.setOnItemClickListener(this);
                break;

            case R.id.subkategori:
//                Toast.makeText(MainActivity.this, "You clicked on : " + subkategoriName.get(postion).toString(), Toast.LENGTH_LONG).show();
                selectedSubkat[selectedKat] = postion;
                imageViews[selectedKat].setImageResource(gbrSubkategori[selectedKat][postion]);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("GBR0", selectedSubkat[0]);
        outState.putInt("GBR1", selectedSubkat[1]);
        outState.putInt("GBR2", selectedSubkat[2]);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int     gbr0 = savedInstanceState.getInt("GBR0"),
                gbr1 = savedInstanceState.getInt("GBR1"),
                gbr2 = savedInstanceState.getInt("GBR2");
        imageViews[0].setImageResource(gbrSubkategori[0][gbr0]);
        imageViews[1].setImageResource(gbrSubkategori[1][gbr1]);
        imageViews[2].setImageResource(gbrSubkategori[2][gbr2]);
        selectedSubkat[0] = gbr0;
        selectedSubkat[1] = gbr1;
        selectedSubkat[2] = gbr2;
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.save){simpanGambar();}
    }
    public void simpanGambar(){
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.canvas);
        Bitmap bitmap = getBitmap(layout);
        saveChart(bitmap, layout.getMeasuredHeight(), layout.getMeasuredWidth());
    }
    public Bitmap getBitmap(RelativeLayout layout){
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(layout.getDrawingCache());
        layout.setDrawingCacheEnabled(false);
        return bmp;
    }

    public void saveChart(Bitmap getbitmap, float height, float width){
        String storage = "";
        //Checking SD Card
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        File folder = null;
        if(isSDPresent) {  // yes SD-card is present and can write media
            storage = "SD";
            folder = new File(String.valueOf(Environment
                    .getExternalStoragePublicDirectory("Avatar Maker")));
                    // .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myfolder");
        }
        else { // Sorry (SD Card can't be read), So write on Internal Storage
            storage = "internal";
            folder = getDir("Avatar Maker", MODE_PRIVATE);
        }

        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        File file = new File(folder.getPath() + File.separator + "/"+timeStamp+".png");

        if ( !file.exists() ) {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream ostream = null;
        try {
            ostream = new FileOutputStream(file);

            System.out.println(ostream);

            Bitmap well = getbitmap;
            Bitmap save = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            Canvas now = new Canvas(save);
            now.drawRect(new Rect(0,0,(int) width, (int) height), paint);
            now.drawBitmap(well,
                    new Rect(0,0,well.getWidth(),well.getHeight()),
                    new Rect(0,0,(int) width, (int) height), null);

            if(save == null) {System.out.println("NULL bitmap save\n");}

            save.compress(Bitmap.CompressFormat.PNG, 100, ostream);

        }catch (NullPointerException e) {
            e.printStackTrace();//Toast.makeText(getApplicationContext(), "Null error", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();// Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            e.printStackTrace(); // Toast.makeText(getApplicationContext(), "IO error", Toast.LENGTH_SHORT).show();
        }finally {
            Toast.makeText(CreateAvatar.this, "File "+timeStamp+" telah tersimpan di memori "+storage, Toast.LENGTH_SHORT).show();
        }
    }

}