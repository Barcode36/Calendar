package project4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.*;
import java.util.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Calendar extends Application
{
   private BorderPane containerPane = new BorderPane();
   private LocalDateTime date = LocalDateTime.now();
   private YearMonth yearMonthView = YearMonth.from(date);
   private String appointmentFile = "src/project4/appointmentFile.csv";
   private Scene scene;

   private HBox topBox = new HBox();
   private String[] firstLetters = { "S", "M", "T", "W", "T", "F", "S" };
   private Month[] months = Month.values();

   private Stage appointmentStage = new Stage();
   private  Scene appointmentScene;
   private GridPane appointmentPane = new GridPane();

   private int appointmentYear = 0;
   private int appointmentMonth = 0;
   private int appointmentDay = 0;
   private boolean appointmentVisible = true;

   private double xOffset = 0;
   private double yOffset =0;

   @Override
   public void start(Stage primaryStage)
   {
      scene = new Scene(containerPane, 1000, 800);
      containerPane.setStyle("-fx-background-color: whitesmoke;");
      setupTopPane();
      GridPane gp = setupMonthPane(date.getYear(), date.getMonthValue());
      containerPane.setCenter(gp);

        
      primaryStage.setTitle("Calendar");
      primaryStage.setMinWidth(1000);
      primaryStage.setMinHeight(800);
      primaryStage.setScene(scene);
      primaryStage.show();

        
        

      appointmentScene = new Scene(appointmentPane, 350, 250);
      setupAppointmentPane();
      appointmentStage.setTitle("Add Event");
      appointmentStage.setScene(appointmentScene);
   }
    
   public void setupTopPane()
   {
      topBox.setPadding(new Insets(15));
      topBox.setSpacing(10);

      Text currentMonth = new Text(date.getMonth() + "");
      currentMonth.setFont(new Font(20));
      Text currentYear = new Text(date.getYear() + "");
      currentYear.setFont(new Font(20));
      Region topSpacer = new Region();

      HBox nav = new HBox();
      Button backBtn = new Button("<");
      Button forwardBtn = new Button(">");
      Button yearBtn = new Button("Year");
      yearBtn.setMinWidth(70);
      Button todayBtn = new Button("Today");
      todayBtn.setMinWidth(70);
      nav.getChildren().addAll(backBtn,yearBtn,todayBtn,forwardBtn);
      nav.setSpacing(5);

      backBtn.setOnAction(event -> {
         yearMonthView = yearMonthView.minusMonths(1);
         currentMonth.setText(yearMonthView.getMonth() + "");
         currentYear.setText(yearMonthView.getYear() + "");
         GridPane previousPane = setupMonthPane(yearMonthView.getYear(),
                 yearMonthView.getMonthValue());
         containerPane.setCenter(previousPane);
      });

      forwardBtn.setOnAction(event -> {
         yearMonthView = yearMonthView.plusMonths(1);
         currentMonth.setText(yearMonthView.getMonth() + "");
         currentYear.setText(yearMonthView.getYear() + "");
         GridPane nextPane = setupMonthPane(yearMonthView.getYear(),
                 yearMonthView.getMonthValue());
         containerPane.setCenter(nextPane);
      });

      yearBtn.setOnAction(event -> {
         currentMonth.setText("");
         currentYear.setText(yearMonthView.getYear() + "");
         yearMonthView = YearMonth.from(date);
         appointmentVisible = false;
         GridPane yearView = twelveMonthsPane();
         containerPane.setCenter(yearView);
         appointmentVisible = true;
      });

      todayBtn.setOnAction(event -> {
         yearMonthView = yearMonthView.from(date);
         currentMonth.setText(date.getMonth() + "");
         currentYear.setText(date.getYear() + "");
         GridPane todayPane = setupMonthPane(yearMonthView.getYear(),
                 yearMonthView.getMonthValue());
         containerPane.setCenter(todayPane);
      });

      topBox.getChildren().addAll(currentMonth,currentYear,topSpacer,nav);
      topBox.setHgrow(topSpacer, Priority.ALWAYS);
      containerPane.setTop(topBox);
   }
   
   public GridPane setupMonthPane(int yearValue, int monthValue)
   {
      GridPane monthPane = new GridPane();
      monthPane.setGridLinesVisible(true);
      monthPane.setAlignment(Pos.TOP_CENTER);

      for(int i = 0; i < 7; i++)
      {
         ColumnConstraints col = new ColumnConstraints();
         col.setPercentWidth(100/7);
         monthPane.getColumnConstraints().add(col);

         if(i == 0)
            monthPane.getRowConstraints().add(new RowConstraints(40));
         else
         {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100/7);
            monthPane.getRowConstraints().add(row);
         }
      }

      for(int col = 0; col < 7; col++)
      {
         for(int row = 0; row < 7; row++)
         {
            if(row == 0)
            {
               Text text = new Text(firstLetters[col]);
               text.setTextAlignment(TextAlignment.CENTER);
               monthPane.add(text, col, row);
               monthPane.setHalignment(text, HPos.CENTER);
            }
         }
      }

      fillUpMonth(monthPane, yearValue, monthValue);
      if(appointmentVisible)
      {
         displayAppointments(monthPane);
      }

      return monthPane;
   }
   
   public void fillUpMonth(GridPane monthGP, int yearValue, int monthValue)
   {
      YearMonth yearMonth = YearMonth.of(yearValue,monthValue);
      YearMonth preMonth = yearMonth.minusMonths(1);
      DayOfWeek firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek();

      int currentMonthDay = 1;
      int nextMonthDays = 1;
      int preMonthDays = 0;
      if (!firstDayOfWeek.equals(DayOfWeek.SUNDAY))
      {
         preMonthDays = firstDayOfWeek.getValue();
      }

      for(int row = 1; row < 7; row++)
      {
         for(int col = 0; col < 7; col++)
         {
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            StackPane sp = new StackPane();
            if(preMonthDays == 0 && currentMonthDay <= yearMonth.lengthOfMonth())
            {

               Text currentDate = new Text(currentMonthDay + "");
               if(currentMonthDay == date.getDayOfMonth() &&
                       (yearMonth.getYear() == date.getYear()) &&
                       (yearMonth.getMonthValue() == date.getMonthValue()))
               {
                  currentDate.setFill(Color.WHITE);
                  Circle circle = new Circle(15, Color.RED);
                  sp.getChildren().addAll(circle, currentDate);
               }
               else
               {
                  sp.getChildren().add(currentDate);
               }
               currentMonthDay++;

               // Listener for a specific day!
               currentDate.setOnMouseClicked(event -> {
                  if(event.getClickCount() == 2)
                  {
                     Object temp = event.getSource();

                     Text test = (Text)temp;
                     appointmentDay = Integer.parseInt(test.getText());
                     appointmentMonth = yearMonthView.getMonthValue();
                     appointmentYear = yearMonthView.getYear();
                     appointmentStage.show();
                  }
               });

            }
            else if(preMonthDays > 0)
            {
               preMonthDays--;
               Text prevDate = new Text(preMonth.lengthOfMonth()- preMonthDays + "");
               prevDate.setFill(Color.GREY);
               sp.getChildren().add(prevDate);

            }
            else
            {
               Text nextDate = new Text(nextMonthDays + "");
               nextDate.setFill(Color.GREY);
               sp.getChildren().add(nextDate);
               nextMonthDays++;
            }
            vbox.getChildren().add(sp);
            monthGP.add(vbox, col, row);
         }
      }
   }
    
   public GridPane twelveMonthsPane()
   {
      GridPane twelve = new GridPane();
      twelve.setHgap(10);
      twelve.setVgap(10);
      twelve.setAlignment(Pos.CENTER);

      for(int i = 0; i < 4; i++)
      {
         if(i < 3) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight((800 - topBox.getHeight()) / 3);
            twelve.getRowConstraints().add(row);
         }
         ColumnConstraints col = new ColumnConstraints();
         col.setPercentWidth(100/4);
         twelve.getColumnConstraints().add(col);

      }

      int monthCount = 0;
      for (int row = 0; row < 3; row++)
      {
         for(int col = 0; col < 4; col++)
         {
            VBox vbox = new VBox();
            Text text = new Text(months[monthCount].name());
            text.setTextAlignment(TextAlignment.CENTER);
            vbox.setAlignment(Pos.CENTER);
            GridPane monthPane = setupMonthPane(yearMonthView.getYear(),
                    months[monthCount].getValue());
            monthPane.setGridLinesVisible(false);
            monthPane.setHgap(2);
            monthPane.setVgap(2);
            vbox.getChildren().addAll(text, monthPane);
            twelve.add(vbox, col, row);
            monthCount++;
         }
      }
      return twelve;
   }
   
   //The following is for the extra credit
    
   public void setupAppointmentPane()
   {
      Label title = new Label("Title: ");
      TextField titleField = new TextField();
      titleField.setPrefColumnCount(2);
      Label time = new Label("Time: ");

      String[] hourArray = {"00","01","02","03","04","05","06",
                           "07","08","09","10","11","12","13",
                           "14","15","16","17","18","19","20",
                           "21","22","23"};

      String[] minutes = {"00","01","02","03","04","05","06",
                          "07","08","09","10","11","12","13",
                          "14","15","16","17","18","19","20",
                          "21","22","23","24","25","26","27",
                          "28","29","30","31","32","33","33",
                          "34","35","36","37","38","39","40",
                          "41","42","43","44","45","46","47",
                          "48","49","50","51","52","53","54",
                          "55","56","57","58","59","60"};

      LocalTime comboTime = LocalTime.now();
      ObservableList<String> hourList = FXCollections.observableArrayList(hourArray);
      ComboBox<String> comboHour = new ComboBox<>(hourList);
      comboHour.getSelectionModel().select(comboTime.getHour()+"");

      ObservableList<String> minuteList = FXCollections.observableArrayList(minutes);
      ComboBox<String> comboMinute = new ComboBox<>(minuteList);
      comboMinute.getSelectionModel().select(comboTime.getMinute()+"");

      Button clear = new Button("Clear");
      Button submit = new Button("Submit");


      // First Row
      appointmentPane.add(title, 0, 0);
      appointmentPane.add(titleField,1, 0,
              2,1);
      // Second Row
      appointmentPane.add(time,0,1);
      appointmentPane.add(comboHour, 1,1);
      appointmentPane.add(comboMinute, 2,1);
      // Third Row
      appointmentPane.add(clear, 1,2);
      appointmentPane.add(submit, 2,2);
      // GridPane properties
      appointmentPane.setHgap(10);
      appointmentPane.setVgap(10);
      appointmentPane.setPadding(new Insets(10));
      appointmentPane.setAlignment(Pos.CENTER);

      submit.setOnAction(event -> {

         int hour = Integer.parseInt(comboHour.getValue());
         int minute =  Integer.parseInt(comboMinute.getValue());

         storeAppointment(titleField.getText(), appointmentYear, appointmentMonth,
                           appointmentDay, hour, minute);

         appointmentStage.close();
         GridPane gp = setupMonthPane(yearMonthView.getYear(), yearMonthView.getMonthValue());
         containerPane.setCenter(gp);
         titleField.clear();
      });
      clear.setOnAction(event -> {
         clear(titleField, comboHour, comboMinute);
      });
   }
    
   public void displayAppointments(GridPane monthPane)
   {
      try(Scanner input = new Scanner(new File(appointmentFile))) {

         if(!input.hasNext()) return; // if file is empty, dont do anything

         while (input.hasNextLine())
         {
            String[] arr = input.nextLine().split(",");

            String appTitle = arr[0];
            int year = Integer.parseInt(arr[1]);
            int month = Integer.valueOf(arr[2]);
            int day = Integer.parseInt(arr[3]);
            String hour = arr[4];
            String minute = arr[5];

            if(year == yearMonthView.getYear() && month == yearMonthView.getMonthValue())
            {
               List list = monthPane.getChildren();
               Iterator<Node> iterator = list.listIterator();

               while (iterator.hasNext())
               {
                  Node node = iterator.next();
                  if(node instanceof VBox)
                  {
                     List vBoxList = ((VBox) node).getChildren();
                     Iterator<Node> vBoxIterator = vBoxList.listIterator();

                     while (vBoxIterator.hasNext())
                     {
                        Node vBoxNode = vBoxIterator.next();
                        if (vBoxNode instanceof StackPane)
                        {
                           StackPane sp = (StackPane)vBoxNode;

                           for( Node spNode: sp.getChildren())
                           {
                              if(spNode instanceof Text)
                              {
                                 Text tempText = (Text) spNode;
                                 int temp = Integer.parseInt(tempText.getText());
                                 if(temp == day &&
                                         (tempText.getFill() == Color.BLACK || tempText.getFill() == Color.WHITE))
                                 {
                                    Label label1 = new Label(hour + ":" + minute + " " + appTitle);
                                    label1.setStyle("-fx-text-fill: green");
                                    label1.setWrapText(true);
                                    label1.setTextAlignment(TextAlignment.CENTER);
                                    ((ListIterator<Node>) vBoxIterator).add(label1);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }


   }

   public void clear(TextField title, ComboBox hour, ComboBox minute)
   {
      title.clear();
      LocalTime comboTime = LocalTime.now();
      hour.getSelectionModel().select(comboTime.getHour()+"");
      minute.getSelectionModel().select(comboTime.getMinute()+"");
   }

    
   public void storeAppointment(String title, int year, int month, int day, int hour, int minute)
   {
      try(FileWriter fileWriter = new FileWriter(appointmentFile, true)) {
         DecimalFormat df = new DecimalFormat("00");
         fileWriter.write(title + ",");
         fileWriter.write(year + ",");
         fileWriter.write(month + ",");
         fileWriter.write(day + ",");
         fileWriter.write(df.format(hour) + ",");
         fileWriter.write(df.format(minute) + "\n");

      }
      catch(FileNotFoundException e) {
         System.out.println("File not Found!");
         e.printStackTrace();
      }
      catch (IOException e) {
         System.out.println("IOException!");
         e.printStackTrace();
      }
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args)
   {
       launch(args);
   }

}
