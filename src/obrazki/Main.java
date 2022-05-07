package obrazki;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static BufferedImage img1;
    public static BufferedImage img2;
    public static BufferedImage edited;
    public static JFrame window;
    public static WindowDrawing wd = new WindowDrawing();
    public static final String prefix = "[M] ";

    public static int Mr=0, Mg=0, Mb=0;
    public static int[] R = new int[256];
    public static int[] G = new int[256];
    public static int[] B = new int[256];

    public static String mode = "";
    public static String[] optionsToChoose = {"Wybierz", "Rozjaśnienie i Przyciemnienie", "Negatyw", "Transformacja Potęgowa", prefix+"Suma",
            prefix+"Odejmowanie", prefix+"Różnica", prefix+"Mnożenie", prefix+"Mnożenie Odwrotności",
            prefix+"Negacja", prefix+"Ciemniejsze", prefix+"Jaśniejsze", prefix+"Wyłączanie",
            prefix+"Nakładka", prefix+"Ostre Światło", prefix+"Łagodne Światło", prefix+"Rozcieńczenie",
            prefix+"Wypalanie", prefix+"Reflect Mode", prefix+"Przezroczystość", "Filtr Robertsa POZIOM", "Filtr Prewitta POZIOM", "Filtr Sobela POZIOM"
            , "Filtr Robertsa PION", "Filtr Prewitta PION", "Filtr Sobela PION", "Filtr Laplacea 1", "Filtr Laplacea 2"
            , "Filtr Laplacea 3", "Filtr Min", "Filtr Max", "Filtr Medianowy", "Kontrast (Zwiekszanie Zmniejszanie)"};

    public static void main(String[] args) {
        try {
            img1 = ImageIO.read(new File("kwiatek.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        window = new JFrame("Obrazki");
        window.setSize(880, 750);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);




        JTextArea data = new JTextArea();
        data.setBounds(240, 10, 200, 50);
        window.add(data);

        JComboBox<String> jComboBox = new JComboBox<>(optionsToChoose);
        jComboBox.setBounds(10, 10, 220, 20);

        jComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mode = (String) jComboBox.getSelectedItem();

                if (mode == null) return;

                if (mode.equals("Rozjaśnienie i Przyciemnienie")) {
                    data.setText("Jasność:50\nKąt nachylenia:1.0");
                } else if (mode.equals("Negatyw")) {
                    data.setText("");
                } else if (mode.equals("Transformacja Potęgowa")) {
                    data.setText("c:0.2\nn:1.5");
                } else if (mode.equals(prefix+"Przezroczystość")){
                    data.setText("Obrazek do mieszania:test.jpg\nPoziom:0.7");
                } else if (mode.equals("Filtr Robertsa POZIOM")) {
                    data.setText("0;0;0\n0;1;-1\n0;0;0");
                } else if (mode.equals("Filtr Prewitta POZIOM")) {
                    data.setText("1;1;1\n0;0;0\n-1;-1;-1");
                } else if (mode.equals("Filtr Sobela POZIOM")) {
                    data.setText("1;2;1\n0;0;0\n-1;-2;-1");
                } else if (mode.equals("Filtr Robertsa PION")) {
                    data.setText("0;0;0\n0;1;0\n0;-1;0");
                } else if (mode.equals("Filtr Prewitta PION")) {
                    data.setText("1;0;-1\n1;0;-1\n1;0;-1");
                } else if (mode.equals("Filtr Sobela PION")) {
                    data.setText("1;0;-1\n2;0;-2\n1;0;-1");
                } else if (mode.equals("Filtr Laplacea 1")) {
                    data.setText("0;-1;0\n-1;4;-1\n0;-1;0");
                } else if (mode.equals("Filtr Laplacea 2")) {
                    data.setText("-1;-1;-1\n-1;8;-1\n-1;-1;-1");
                } else if (mode.equals("Filtr Laplacea 3")) {
                    data.setText("-2;1;-2\n1;4;1\n-2;1;-2");
                } else if (mode.equals("Filtr Min")) {
                    data.setText("Size: 1");
                } else if (mode.equals("Filtr Max")) {
                    data.setText("Size: 1");
                } else if (mode.equals("Filtr Medianowy")) {
                    data.setText("Size: 1");
                } else if (mode.equals("Kontrast (Zwiekszanie Zmniejszanie)")) {
                    data.setText("Kontrast[-128,127]:63");
                } else {
                     data.setText("Obrazek do mieszania:test.jpg");
                }
            }
        });
        window.add(jComboBox);



        JButton btn = new JButton();
        btn.setText("Update");
        btn.setBounds(460, 10, 200, 50);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String info = data.getText();
                String[] datainfo = info.split("\n");

                if (mode.equals("Rozjaśnienie i Przyciemnienie")) {
                    double a = 1.0;
                    int b = 0;
                    for (int i = 0; i < datainfo.length; i++) {
                        if (datainfo[i].startsWith("Jasność:")) {
                            datainfo[i] = datainfo[i].replace("Jasność:", "");

                            try {
                                b = Integer.parseInt(datainfo[i]);
                            } catch (Exception exc) {
                                data.setText(data.getText()+"\nBład wartości jasności.");
                            }
                        } else if (datainfo[i].startsWith("Kąt nachylenia:")) {
                            datainfo[i] = datainfo[i].replace("Kąt nachylenia:", "");

                            try {
                                a = Double.parseDouble(datainfo[i]);
                            } catch (Exception exc) {
                                data.setText(data.getText()+"\nBład wartości Kąta nachylenia.");
                            }
                        }
                    }
                    rozjasnienie(a, b);
                } else if (mode.equals("Negatyw")) {
                    negatyw();
                } else if (mode.equals("Transformacja Potęgowa")) {
                    double c = 1.0;
                    double n = 0.0;
                    for (int i = 0; i < datainfo.length; i++) {
                        if (datainfo[i].startsWith("c:")) {
                            datainfo[i] = datainfo[i].replace("c:", "");

                            try {
                                c = Double.parseDouble(datainfo[i]);
                            } catch (Exception exc) {
                                data.setText(data.getText()+"\nBład wartości c.");
                            }
                        } else if (datainfo[i].startsWith("n:")) {
                            datainfo[i] = datainfo[i].replace("n:", "");

                            try {
                                n = Double.parseDouble(datainfo[i]);
                            } catch (Exception exc) {
                                data.setText(data.getText()+"\nBład wartości n.");
                            }
                        }
                    }
                    transformacjapotegowa(c,n);
                } else if (mode.equals(prefix+"Przezroczystość")) {
                    double alpha = 1.0;
                    for (int i = 0; i < datainfo.length; i++) {
                        if (datainfo[i].startsWith("Poziom:")) {
                            datainfo[i] = datainfo[i].replace("Poziom:", "");

                            try {
                                alpha = Double.parseDouble(datainfo[i]);
                            } catch (Exception exc) {
                                data.setText(data.getText()+"\nBład wartości alpha.");
                            }
                        } else if (datainfo[i].startsWith("Obrazek do mieszania:")) {
                            datainfo[i] = datainfo[i].replace("Obrazek do mieszania:", "");

                            try {
                                img2 = ImageIO.read(new File(datainfo[i]));
                            } catch (Exception exc) {
                                data.setText(data.getText() + "\nBład wartości Obrazek do mieszania.");
                            }
                        }
                    }
                    mprzezroczystosc(img2, alpha);
                } else if (mode.equals("Filtr Robertsa POZIOM") || mode.equals("Filtr Prewitta POZIOM") || mode.equals("Filtr Sobela POZIOM")
                        || mode.equals("Filtr Robertsa PION") || mode.equals("Filtr Prewitta PION") || mode.equals("Filtr Sobela PION")
                        || mode.equals("Filtr Laplacea 1") || mode.equals("Filtr Laplacea 2") || mode.equals("Filtr Laplacea 3")) {
                    int[][] mask = new int[3][3];
                    int id = 0;
                    for (int i = 0; i < datainfo.length; i++) {
                        String[] maskdata = datainfo[i].split(";");
                        for (int j = 0; j < maskdata.length; j++) {
                            mask[id][j] = Integer.parseInt(maskdata[j]);
                        }
                        id++;
                    }
                    maska3(mask);
                } else if (mode.equals("Filtr Min")) {
                    int size = 1;
                    if (datainfo[0].contains("Size: ")) {
                        size = Integer.parseInt(datainfo[0].replace("Size: ", ""));
                    }
                    filtrmin(size);
                } else if (mode.equals("Filtr Max")) {
                    int size = 1;
                    if (datainfo[0].contains("Size: ")) {
                        size = Integer.parseInt(datainfo[0].replace("Size: ", ""));
                    }
                    filtrmax(size);
                } else if (mode.equals("Filtr Medianowy")) {
                    int size = 1;
                    if (datainfo[0].contains("Size: ")) {
                        size = Integer.parseInt(datainfo[0].replace("Size: ", ""));
                    }
                    filtrmediana(size);
                } else if (mode.equals("Kontrast (Zwiekszanie Zmniejszanie)")) {
                    double c = 1.0;
                    for (int i = 0; i < datainfo.length; i++) {
                        if (datainfo[i].startsWith("Kontrast[-128,127]:")) {
                            datainfo[i] = datainfo[i].replace("Kontrast[-128,127]:", "");

                            try {
                                c = Double.parseDouble(datainfo[i]);
                            } catch (Exception exc) {
                                data.setText(data.getText()+"\nBład wartości jasności.");
                            }
                        }
                    }
                    kontrast(c);
                } else {
                    for (int i = 0; i < datainfo.length; i++) {
                        if (datainfo[i].startsWith("Obrazek do mieszania:")) {
                            datainfo[i] = datainfo[i].replace("Obrazek do mieszania:", "");

                            try {
                                img2 = ImageIO.read(new File(datainfo[i]));
                            } catch (Exception exc) {
                                data.setText(data.getText() + "\nBład wartości Obrazek do mieszania.");
                            }
                        }
                    }

                    if (mode.equals(prefix + "Suma")) {
                        msuma(img2);
                    } else if (mode.equals(prefix + "Odejmowanie")) {
                        modejmowanie(img2);
                    } else if (mode.equals(prefix + "Różnica")) {
                        mroznica(img2);
                    } else if (mode.equals(prefix + "Mnożenie")) {
                        mmnozenie(img2);
                    } else if (mode.equals(prefix + "Mnożenie Odwrotności")) {
                        mmnozenieodwrotnoci(img2);
                    } else if (mode.equals(prefix + "Negacja")) {
                        mnegacja(img2);
                    } else if (mode.equals(prefix + "Ciemniejsze")) {
                        mciemniejsze(img2);
                    } else if (mode.equals(prefix + "Jaśniejsze")) {
                        mjasniejsze(img2);
                    } else if (mode.equals(prefix + "Wyłączanie")) {
                        mwylaczanie(img2);
                    } else if (mode.equals(prefix + "Nakładka")) {
                        mnakladka(img2);
                    }  else if (mode.equals(prefix + "Ostre Światło")) {
                        mostreswiatlo(img2);
                    } else if (mode.equals(prefix + "Łagodne Światło")) {
                        mlagodneswiatlo(img2);
                    } else if (mode.equals(prefix + "Rozcieńczenie")) {
                        mrozcienczenie(img2);
                    } else if (mode.equals(prefix + "Wypalanie")) {
                        mwypalenie(img2);
                    } else if (mode.equals(prefix + "Reflect Mode")) {
                        mreflectmode(img2);
                    }
                    // JUMP
                }

                histogram();
            }
        });

        window.add(btn);


        window.add(wd);
        window.setVisible(true);

//        File file1 = new File("kwiatek1.jpeg");
//        try {
//            ImageIO.write(img1, "jpeg", file1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


//     else if (mode.equals(prefix+"Suma")) {
//        for (int i = 0; i < datainfo.length; i++) {
//            if (datainfo[i].startsWith("Obrazek do mieszania:")) {
//                datainfo[i] = datainfo[i].replace("Obrazek do mieszania:", "");
//
//                try {
//                    img2 = ImageIO.read(new File(datainfo[i]));
//                } catch (Exception exc) {
//                    data.setText(data.getText()+"\nBład wartości Obrazek do mieszania.");
//                }
//            }
//        }
//        msuma(img2);
//    } else if (mode.equals(prefix+"Odejmowanie")) {
//        for (int i = 0; i < datainfo.length; i++) {
//            if (datainfo[i].startsWith("Obrazek do mieszania:")) {
//                datainfo[i] = datainfo[i].replace("Obrazek do mieszania:", "");
//
//                try {
//                    img2 = ImageIO.read(new File(datainfo[i]));
//                } catch (Exception exc) {
//                    data.setText(data.getText()+"\nBład wartości Obrazek do mieszania.");
//                }
//            }
//        }
//        modejmowanie(img2);
//    } else if (mode.equals(prefix+"Różnica")) {
//        for (int i = 0; i < datainfo.length; i++) {
//            if (datainfo[i].startsWith("Obrazek do mieszania:")) {
//                datainfo[i] = datainfo[i].replace("Obrazek do mieszania:", "");
//
//                try {
//                    img2 = ImageIO.read(new File(datainfo[i]));
//                } catch (Exception exc) {
//                    data.setText(data.getText()+"\nBład wartości Obrazek do mieszania.");
//                }
//            }
//        }
//        mroznica(img2);
//    } else if (mode.equals(prefix+"Mnożenie")) {
//        for (int i = 0; i < datainfo.length; i++) {
//            if (datainfo[i].startsWith("Obrazek do mieszania:")) {
//                datainfo[i] = datainfo[i].replace("Obrazek do mieszania:", "");
//
//                try {
//                    img2 = ImageIO.read(new File(datainfo[i]));
//                } catch (Exception exc) {
//                    data.setText(data.getText()+"\nBład wartości Obrazek do mieszania.");
//                }
//            }
//        }
//        mmnozenie(img2);
//    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static void rozjasnienie(double a, int b) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color c = new Color(edited.getRGB(x, y));

                int re = (int) (c.getRed() * a) + b;
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = (int) (c.getGreen() * a) + b;
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = (int) (c.getBlue() * a) + b;
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void negatyw() {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color c = new Color(edited.getRGB(x, y));

                int re = 255 - c.getRed();
                int gr = 255 - c.getGreen();
                int bl = 255 - c.getBlue();

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void transformacjapotegowa(double c, double n) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));

                int re = (int) Math.pow((co.getRed() * c), n);
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = (int) Math.pow((co.getGreen() * c), n);
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = (int) Math.pow((co.getBlue() * c), n);
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void msuma(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = co.getRed() + co2.getRed();
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = co.getGreen() + co2.getGreen();
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = co.getBlue() + co2.getBlue();
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void modejmowanie(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = co.getRed() + co2.getRed() - 1;
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = co.getGreen() + co2.getGreen() - 1;
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = co.getBlue() + co2.getBlue() - 1;
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void mroznica(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = Math.abs(co.getRed() - co2.getRed());
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = Math.abs(co.getGreen() - co2.getGreen());
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = Math.abs(co.getBlue() - co2.getBlue());
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void mmnozenie(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = co.getRed() * co2.getRed();
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = co.getGreen() * co2.getGreen();
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = co.getBlue() * co2.getBlue();
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void mmnozenieodwrotnoci(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = (1 - (1 - co.getRed()) * (1 - co2.getRed()));
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = (1 - (1 - co.getGreen()) * (1 - co2.getGreen()));
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = (1 - (1 - co.getBlue()) * (1 - co2.getBlue()));
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void mnegacja(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = 1 - Math.abs(1 - co.getRed() - co2.getRed());
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = 1 - Math.abs(1 - co.getGreen() - co2.getGreen());
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = 1 - Math.abs(1 - co.getBlue() - co2.getBlue());
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void mciemniejsze(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = co2.getRed();
                if (co.getRed() < co2.getRed()) {
                    re = co.getRed();
                }
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = co2.getGreen();
                if (co.getGreen() < co2.getGreen()) {
                    gr = co.getGreen();
                }
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = co2.getBlue();
                if (co.getBlue() < co2.getBlue()) {
                    bl = co.getBlue();
                }
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void mjasniejsze(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = co2.getRed();
                if (co.getRed() > co2.getRed()) {
                    re = co.getRed();
                }
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = co2.getGreen();
                if (co.getGreen() > co2.getGreen()) {
                    gr = co.getGreen();
                }
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = co2.getBlue();
                if (co.getBlue() > co2.getBlue()) {
                    bl = co.getBlue();
                }
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void mwylaczanie(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = co.getRed() + co2.getRed() - (2 * co.getRed() * co2.getRed());
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = co.getGreen() + co2.getGreen() - (2 * co.getGreen() * co2.getGreen());
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = co.getBlue() + co2.getBlue() - (2 * co.getBlue() * co2.getBlue());
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void mnakladka(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = 1 - (2 * (1 - co.getRed()) * (1 - co2.getRed()));
                if (co.getRed() < 255 * 0.5) {
                    re = 2 * co.getRed() * co2.getRed();
                }
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = 1 - (2 * (1 - co.getGreen()) * (1 - co2.getGreen()));
                if (co.getGreen() < 255 * 0.5) {
                    gr = 2 * co.getGreen() * co2.getGreen();
                }
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = 1 - (2 * (1 - co.getBlue()) * (1 - co2.getBlue()));
                if (co.getBlue() < 255 * 0.5) {
                    bl = 2 * co.getBlue() * co2.getBlue();
                }
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void mostreswiatlo(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = 1 - (2 * (1 - co.getRed()) * (1 - co2.getRed()));
                if (co2.getRed() < 255 * 0.5) {
                    re = 2 * co.getRed() * co2.getRed();
                }
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = 1 - (2 * (1 - co.getGreen()) * (1 - co2.getGreen()));
                if (co2.getGreen() < 255 * 0.5) {
                    gr = 2 * co.getGreen() * co2.getGreen();
                }
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = 1 - (2 * (1 - co.getBlue()) * (1 - co2.getBlue()));
                if (co2.getBlue() < 255 * 0.5) {
                    bl = 2 * co.getBlue() * co2.getBlue();
                }
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void mlagodneswiatlo(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = (int) (Math.sqrt(co.getRed()) * (2 * co2.getRed() - 1) + (2 * co.getRed()) * (1 - co2.getRed()));
                if (co2.getRed() < 255 * 0.5) {
                    re = (int) (2 * co.getRed() * co2.getRed() + Math.pow(co.getRed(), 2) * (1 - 2 * co2.getRed()));
                }
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = (int) (Math.sqrt(co.getGreen()) * (2 * co2.getGreen() - 1) + (2 * co.getGreen()) * (1 - co2.getGreen()));
                if (co2.getGreen() < 255 * 0.5) {
                    gr = (int) (2 * co.getGreen() * co2.getGreen() + Math.pow(co.getGreen(), 2) * (1 - 2 * co2.getGreen()));
                }
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = (int) (Math.sqrt(co.getBlue()) * (2 * co2.getBlue() - 1) + (2 * co.getBlue()) * (1 - co2.getBlue()));
                if (co2.getBlue() < 255 * 0.5) {
                    bl = (int) (2 * co.getBlue() * co2.getBlue() + Math.pow(co.getBlue(), 2) * (1 - 2 * co2.getBlue()));
                }
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void mrozcienczenie(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int temp = 1 - co2.getRed();
                if (temp == 0) {
                    temp = 1;
                }
                int re = co.getRed() / (temp);
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                temp = 1 - co2.getGreen();
                if (temp == 0) {
                    temp = 1;
                }
                int gr = co.getGreen() / (temp);
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                temp = 1 - co2.getBlue();
                if (temp == 0) {
                    temp = 1;
                }
                int bl = co.getBlue() / (temp);
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void mwypalenie(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = 1 - (1 - co.getRed()) / (co2.getRed() + 1);
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = 1 - (1 - co.getGreen()) / (co2.getGreen() + 1);
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = 1 - (1 - co.getBlue()) / (co2.getBlue() + 1);
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void mreflectmode(BufferedImage img2) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = (int) (Math.pow(co.getRed(), 2) / (1 - co2.getRed()));
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = (int) (Math.pow(co.getGreen(), 2) / (1 - co2.getGreen()));
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = (int) (Math.pow(co.getBlue(), 2) / (1 - co2.getBlue()));
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void mprzezroczystosc(BufferedImage img2, double alpha) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color co = new Color(edited.getRGB(x, y));
                Color co2 = new Color(img2.getRGB(x, y));

                int re = (int) ((1 - alpha) * co2.getRed() + alpha * co.getRed());
                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                int gr = (int) ((1 - alpha) * co2.getGreen() + alpha * co.getGreen());
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                int bl = (int) ((1 - alpha) * co2.getBlue() + alpha * co.getBlue());
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void maska3(int[][] mask) {
        edited = deepCopy(img1);

        for (int x = 1; x < edited.getWidth() - 1; x++) {
            for (int y = 1; y < edited.getHeight()-1; y++) {
                Color c = new Color(img1.getRGB(x, y));
                int re = 0;
                int gr = 0;
                int bl = 0;

                for (int ix = -1; ix <= 1; ix++) {
                    for (int iy = -1; iy <= 1; iy++) {
                        Color c2 = new Color(img1.getRGB(x+ix, y+iy));

                        re += c2.getRed() * mask[ix+1][iy+1];
                        gr += c2.getGreen() * mask[ix+1][iy+1];
                        bl += c2.getBlue() * mask[ix+1][iy+1];
                    }
                }

//                re = Math.abs(re);
//                gr = Math.abs(gr);
//                bl = Math.abs(bl);
//
//                if (re > 255) re = 255;
//                else if (re < 0) re = 0;
//                if (gr > 255) gr = 255;
//                else if (gr < 0) gr = 0;
//                if (bl > 255) bl = 255;
//                else if (bl < 0) bl = 0;

                if (re > 255) re = 0;
                else if (re < 0) re = 0;
                if (gr > 255) gr = 0;
                else if (gr < 0) gr = 0;
                if (bl > 255) bl = 0;
                else if (bl < 0) bl = 0;

                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }



    public static void filtrmin(int size) {
        edited = deepCopy(img1);

        for (int x = size; x < edited.getWidth() - size; x++) {
            for (int y = size; y < edited.getHeight()-size; y++) {
                int re = 255;
                int gr = 255;
                int bl = 255;

                for (int ix = -size; ix <= size; ix++) {
                    for (int iy = -size; iy <= size; iy++) {
                        Color c2 = new Color(img1.getRGB(x+ix, y+iy));

                        if (re > c2.getRed()) {
                            re = c2.getRed();
                        }
                        if (gr > c2.getGreen()) {
                            gr = c2.getGreen();
                        }
                        if (bl > c2.getBlue()) {
                            bl = c2.getBlue();
                        }
                    }
                }

                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;


                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void filtrmax(int size) {
        edited = deepCopy(img1);

        for (int x = size; x < edited.getWidth() - size; x++) {
            for (int y = size; y < edited.getHeight()-size; y++) {
                int re = 0;
                int gr = 0;
                int bl = 0;

                for (int ix = -size; ix <= size; ix++) {
                    for (int iy = -size; iy <= size; iy++) {
                        Color c2 = new Color(img1.getRGB(x+ix, y+iy));

                        if (re < c2.getRed()) {
                            re = c2.getRed();
                        }
                        if (gr < c2.getGreen()) {
                            gr = c2.getGreen();
                        }
                        if (bl < c2.getBlue()) {
                            bl = c2.getBlue();
                        }
                    }
                }

                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;


                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }

    public static void filtrmediana(int size) {
        edited = deepCopy(img1);

        for (int x = size; x < edited.getWidth() - size; x++) {
            for (int y = size; y < edited.getHeight()-size; y++) {
                int re = 0;
                int gr = 0;
                int bl = 0;

                ArrayList<Integer> reds = new ArrayList<>();
                ArrayList<Integer> greens = new ArrayList<>();
                ArrayList<Integer> blues = new ArrayList<>();

                for (int ix = -size; ix <= size; ix++) {
                    for (int iy = -size; iy <= size; iy++) {
                        Color c2 = new Color(img1.getRGB(x+ix, y+iy));

                        reds.add(c2.getRed());
                        greens.add(c2.getGreen());
                        blues.add(c2.getBlue());

                    }
                }


                Collections.sort(reds);
                if (reds.size() % 2 == 0) {
                    re = reds.get(reds.size() / 2) + reds.get(reds.size() / 2 - 1) / 2;
                } else {
                    re = reds.get(reds.size() / 2);
                }
                Collections.sort(greens);
                if (greens.size() % 2 == 0) {
                    gr = greens.get(greens.size() / 2) + greens.get(greens.size() / 2 - 1) / 2;
                } else {
                    gr = greens.get(greens.size() / 2);
                }
                Collections.sort(blues);
                if (blues.size() % 2 == 0) {
                    bl = blues.get(blues.size() / 2) + blues.get(blues.size() / 2 - 1) / 2;
                } else {
                    bl = blues.get(blues.size() / 2);
                }


                if (re > 255) re = 255;
                else if (re < 0) re = 0;
                if (gr > 255) gr = 255;
                else if (gr < 0) gr = 0;
                if (bl > 255) bl = 255;
                else if (bl < 0) bl = 0;


                Color c2 = new Color(re, gr, bl);
                edited.setRGB(x, y, c2.getRGB());
            }
        }

        wd.repaint();
    }


    public static void kontrast(double contrast) {
        edited = deepCopy(img1);

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color c = new Color(edited.getRGB(x, y));

                if (contrast < 0) {
                    int re = (int) (((127 + contrast) / 127) * c.getRed() - contrast);
                    if (re > 255) re = 255;
                    else if (re < 0) re = 0;
                    int gr = (int) (((127 + contrast) / 127) * c.getGreen() - contrast);
                    if (gr > 255) gr = 255;
                    else if (gr < 0) gr = 0;
                    int bl = (int) (((127 + contrast) / 127) * c.getBlue() - contrast);
                    if (bl > 255) bl = 255;
                    else if (bl < 0) bl = 0;

                    Color c2 = new Color(re, gr, bl);
                    edited.setRGB(x, y, c2.getRGB());
                } else {
                    int re = (int) ((127 / (127 - contrast)) * (c.getRed() - contrast));
                    if (re > 255) re = 255;
                    else if (re < 0) re = 0;
                    int gr = (int) ((127 / (127 - contrast)) * (c.getGreen() - contrast));
                    if (gr > 255) gr = 255;
                    else if (gr < 0) gr = 0;
                    int bl = (int) ((127 / (127 - contrast)) * (c.getBlue() - contrast));
                    if (bl > 255) bl = 255;
                    else if (bl < 0) bl = 0;

                    Color c2 = new Color(re, gr, bl);
                    edited.setRGB(x, y, c2.getRGB());
                }

            }
        }

        wd.repaint();
    }

    public static void histogram() {
        if (edited != null) {
            Mr = 0;
            Mg = 0;
            Mb = 0;
            R = new int[256];
            G = new int[256];
            B = new int[256];

            for (int i = 0; i < edited.getWidth(); i++) {
                for (int j = 0; j < edited.getHeight(); j++) {
                    Color color = new Color(edited.getRGB(i, j));
                    int r = color.getRed();
                    int g = color.getGreen();
                    int b = color.getBlue();
                    R[r]++;
                    G[g]++;
                    B[b]++;
                }
            }
            for (int i = 0; i < 256; i++) {
                if (R[i] > Mr)
                    Mr = R[i];
                if (G[i] > Mg)
                    Mg = G[i];
                if (B[i] > Mb)
                    Mb = B[i];
            }
            wd.repaint();
        } else {
            for (int i = 0; i < img1.getWidth(); i++) {
                for (int j = 0; j < img1.getHeight(); j++) {
                    Color color = new Color(img1.getRGB(i, j));
                    int r = color.getRed();
                    int g = color.getGreen();
                    int b = color.getBlue();
                    R[r]++;
                    G[g]++;
                    B[b]++;
                }
            }
            for (int i = 0; i < 256; i++) {
                if (R[i] > Mr)
                    Mr = R[i];
                if (G[i] > Mg)
                    Mg = G[i];
                if (B[i] > Mb)
                    Mb = B[i];
            }
            wd.repaint();
        }
    }

}
