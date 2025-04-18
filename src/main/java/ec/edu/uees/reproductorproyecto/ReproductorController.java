package ec.edu.uees.reproductorproyecto;

import ec.edu.uees.Lista.DoublyLinkedList;
import ec.edu.uees.Modelo.Cola;
import ec.edu.uees.Modelo.Paciente;
import ec.edu.uees.Modelo.Video;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ReproductorController implements Initializable{
    
    @FXML private AnchorPane root;
    @FXML private StackPane stackPane;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox vboxTurno, vboxVolumen, vboxTurnos;
    @FXML private HBox hboxBotones, hboxVolumen;
    
    @FXML private Label titulo, tituloProyecto, labelVolumen, iconVolumen, labelTurnoActual, turnoActual, labelTurnosCola;
    private double xOffset = 0;
    private double yOffset = 0;
    private double fullScreenOffset = 0;
    private double btnOffset = 1;
    private long lastClickTime = 0;
    
    @FXML private Button cerrar, minimizar, maximizar, btnPlay, btnPrevious, btnNext, btnVentanas;
    private Stage stage;
    boolean callado = false;

    private String filePath = "videos.txt";
    private DoublyLinkedList<Video> videos = new DoublyLinkedList<>();
    
    private ListIterator<Video> lit;
    private Video videoactual;
    
    @FXML private MediaView mediaView;
    private MediaPlayer mediaPlayer;
    private Media media;
    private String videoPath;
    @FXML private Slider sliderVolumen;
    
    private OperacionesArchivo operaciones = new OperacionesArchivo();
    private List<Paciente> pacientes = operaciones.leerPacientes();

    private Cola cola = Cola.getInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        
        root.setOnMouseDragged(e -> {
            stage = (Stage) cerrar.getScene().getWindow();
            if(!stage.isFullScreen()){
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            }
        });
        
        stackPane.setOnMouseDragged(e -> {
            stage = (Stage) cerrar.getScene().getWindow();
            stage.setFullScreen(false);
            resetearNodos(stage);
        });

       try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String direccion = line.replace("\"", "").trim();
                File file = new File(direccion);
                if (!file.exists()) {
                    System.out.println("Archivo no encontrado: " + direccion);
                    continue;
                }
                String titulo = file.getName();
                String rutaJavaFX = file.toPath().toAbsolutePath().toString();
                videos.addLast(new Video(rutaJavaFX, titulo));
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo videos.txt: " + e.getMessage());
            e.printStackTrace();
        }
        
        if(videos.isEmpty()){
            System.out.println("NO hay videos en videos.txt");
        } else {
            System.out.println("SI hay videos en videos.txt");
            videoactual = videos.get(0);
            lit = videos.listIterator(); 
            videoPath = new File(videoactual.getUrl()).toURI().toString();
            media = new Media(videoPath);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.setPreserveRatio(false);
            titulo.setText(videoactual.getTitulo());

            sliderVolumen.valueProperty().addListener((obs, oldVal, newVal) -> {
                mediaPlayer.setVolume(newVal.doubleValue() / 100.0);
                vboxVolumen.setVisible(true);
                labelVolumen.setText(((int) newVal.doubleValue()) + "%");

                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> vboxVolumen.setVisible(false)));
                timeline.setCycleCount(1);
                timeline.play();

                 if(mediaPlayer.getVolume() != 0) {
                    iconVolumen.setText("🔊");
                    iconVolumen.setOnMouseClicked(e -> {
                        sliderVolumen.setValue(0);
                    });
                } 
                if(mediaPlayer.getVolume() == 0) {
                    iconVolumen.setText("🔇");
                    iconVolumen.setOnMouseClicked(e -> {
                        sliderVolumen.setValue(100);
                    });
                }

            });

            if(mediaPlayer.getVolume() != 0) {
                iconVolumen.setText("🔊");
                iconVolumen.setOnMouseClicked(e -> {
                    sliderVolumen.setValue(0);
                    iconVolumen.setText("🔇");
                });
            }

            System.out.println("Video listo, esperando 500ms para iniciar...");
            new Timeline(new KeyFrame(Duration.millis(500), e -> mediaPlayer.play())).play();
            System.out.println("Video corriendo exitosamente"); 

            mediaPlayer.setOnError(() -> {
                String errorMsg = mediaPlayer.getError().getMessage();
                System.out.println("Error de reproducción: " + errorMsg);                    
                System.out.println("Reinciando stage.\n");
                restart();
            });
            
            mediaPlayer.setOnEndOfMedia(() -> {
                if (lit.hasNext()) { 
                    videoactual = lit.next();  
                    cargarVideo(videoactual);
                    iconVolumen.setText("🔊");
                    sliderVolumen.setValue(100);
                    callado = false;
                }
            });
        }
        
        
        sincronizarVbox();
        cola.getObservableCola().addListener((ListChangeListener<Paciente>) c -> sincronizarVbox());
    }
    
    private void sincronizarVbox() {
        Platform.runLater(() -> {
            vboxTurnos.getChildren().clear();
            for (Paciente p : cola.getObservableCola()) {
                Label turno = new Label("Turno " + p.getNumeroTurno());
                turno.setStyle("-fx-font-size: 20px;");
                Label nombre = new Label(p.getNombres() + " " + p.getApellidos());
                nombre.setId("nombreTurnoCola");
                nombre.setWrapText(true);
                vboxTurnos.getChildren().addAll(turno, nombre);
            }
            if(!cola.getObservableCola().isEmpty()) {
                turnoActual.setText(Integer.toString(cola.getColaPrioridad().peek().getNumeroTurno()));    
            } else {
                turnoActual.setText("N/A");
            } 
        });
    }
    
    @FXML
    private void cerrarStage() {
        stage = (Stage) cerrar.getScene().getWindow();
        stage.close();
        if(mediaPlayer != null){
            mediaPlayer.dispose();    
        }
    }
    @FXML
    private void minimizarStage() {
        stage = (Stage) minimizar.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    private void maximizarStage() {
        stage = (Stage) maximizar.getScene().getWindow();
        if(stage.isFullScreen()) {
            stage.setFullScreen(false);
            resetearNodos(stage);
        } else {
            stage.setFullScreen(true);
            ajustarNodos(stage);
        } 
    }
    
    // Codigo extra para acomodar nodos manualmente
    private void ajustarNodos(Stage stage) {
        root.setPrefWidth(stage.getWidth());
        root.setPrefHeight(stage.getHeight());
        stackPane.setPrefWidth(stage.getWidth());
        vboxTurno.setPrefSize(stage.getWidth() / 4, stage.getHeight() / 1.0751);
        hboxBotones.setPrefSize(stage.getWidth() / 1.371, stage.getHeight() / 8.45588235);
        mediaView.setFitHeight(stage.getHeight() / 1.24);
        mediaView.setFitWidth(stage.getWidth() / 1.371);
        vboxTurno.setTranslateY(1);
        
        vboxTurno.setLayoutX(stage.getWidth() / 1.35869565);
        hboxBotones.setLayoutY(stage.getHeight() / 1.15230461);
        titulo.setLayoutY(stage.getHeight() / 1.23);
        vboxTurno.setTranslateX(10);
        fullScreenOffset = 7;
                
        tituloProyecto.setStyle("-fx-font-size: 23px;");
        tituloProyecto.setPrefWidth(300);
        
        labelTurnoActual.setStyle("-fx-font-size: 35px;");
        turnoActual.setStyle("-fx-font-size: 60px;");
        turnoActual.setPrefHeight(turnoActual.getHeight() + 40);
        labelTurnosCola.setStyle("-fx-font-size: 35px;");
        turnoActual.setTranslateY(-5);
        btnVentanas.setPrefSize(stage.getWidth() / 4.35, stage.getHeight() / (11 + btnOffset));
        vboxTurnos.setPrefSize(stage.getWidth() / 5, stage.getHeight() / 1.8);
        scrollPane.setPrefSize(stage.getWidth() / 4.35, stage.getHeight() / 1.77);
        vboxTurnos.setPrefSize(stage.getWidth() / 4.5, stage.getHeight() * 2);
        if(btnOffset == 0) {
            btnVentanas.setTranslateY(3);
        }
        btnOffset = 0;
        btnVentanas.setStyle("-fx-font-size: 25px;");
        
        btnPrevious.setPrefSize(btnPrevious.getWidth() * 2, btnPrevious.getHeight() * 2);
        btnPlay.setPrefSize(btnPlay.getWidth() * 2, btnPlay.getHeight() * 2);
        btnNext.setPrefSize(btnNext.getWidth() * 2, btnNext.getHeight() * 2);
        btnPrevious.setStyle("-fx-background-color: transparent;" + "-fx-font-size: 60px;");
        btnPlay.setStyle("-fx-background-color: transparent;" + "-fx-font-size: 70px;");
        btnNext.setStyle("-fx-background-color: transparent;" + "-fx-font-size: 60px;");
        
        iconVolumen.setStyle("-fx-font-size: 45px;");
        iconVolumen.setPrefSize(iconVolumen.getWidth() * 1.5, iconVolumen.getHeight() * 1.5);
        sliderVolumen.setPrefSize(sliderVolumen.getWidth() * 1.5, sliderVolumen.getHeight() * 1.5);
        hboxVolumen.setPrefSize(hboxVolumen.getWidth() * 1.5, hboxVolumen.getHeight() * 1.5);
        
        vboxVolumen.setLayoutX(stage.getWidth() / 3.24);
        vboxVolumen.setLayoutY(stage.getHeight() / 2.68);
        vboxTurnos.setSpacing(4);
    }
    
    // Codigo extra para acomodar nodos manualmente
    private void resetearNodos(Stage stage) {
        root.setPrefWidth(1000);
        root.setPrefHeight(575);
        stackPane.setPrefWidth(1000);
        vboxTurno.setPrefSize(250, 520);
        hboxBotones.setPrefSize(720, 68);
        mediaView.setFitHeight(450);
        mediaView.setFitWidth(720);
        
        vboxTurno.setLayoutX(739 - fullScreenOffset);
        hboxBotones.setLayoutY(499);
        titulo.setLayoutY(467);
                
        tituloProyecto.setStyle("-fx-font-size: 15px;");
        tituloProyecto.setPrefWidth(150);
        
        labelTurnoActual.setStyle("-fx-font-size: 20px;");
        turnoActual.setStyle("-fx-font-size: 38px;");
        turnoActual.setPrefHeight(50);
        labelTurnosCola.setStyle("-fx-font-size: 20px;");
        vboxTurno.setSpacing(4);
        btnVentanas.setPrefSize(226, 46);
        vboxTurnos.setPrefSize(200, 1000);
        vboxTurno.setSpacing(2);
        scrollPane.setPrefSize(226, 324);
        btnVentanas.setStyle("-fx-font-size: 15px;");
        
        btnPrevious.setPrefSize(65, 66);
        btnPlay.setPrefSize(82, 86);
        btnNext.setPrefSize(65, 66);
        btnPrevious.setStyle("-fx-font-size: 30px;" + "-fx-background-color: transparent;");
        btnPlay.setStyle("-fx-font-size: 35px;"+ "-fx-background-color: transparent;");
        btnNext.setStyle("-fx-font-size: 30px;" + "-fx-background-color: transparent;");
        hboxBotones.setSpacing(10);
        hboxVolumen.setTranslateX(20);
        
        iconVolumen.setStyle("-fx-font-size: 28px;");
        iconVolumen.setPrefSize(46, 40);
        sliderVolumen.setPrefSize(140,14);
        hboxVolumen.setPrefSize(199, 68);
        
        vboxVolumen.setLayoutX(304);
        vboxVolumen.setLayoutY(214);
    }
    
    @FXML
    private void play() {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            btnPlay.setText("▶");
        } else {
            mediaPlayer.play();
            btnPlay.setText("⏸");
        }
    }
    
    @FXML 
    private void next() {
        if (lit.hasNext()) {
            videoactual = lit.next();
            cargarVideo(videoactual);
        }
                
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            btnPlay.setText("▶");
        } else {
            btnPlay.setText("⏸");
        }
        sliderVolumen.setValue(100);
    }

    @FXML
    private void previous() {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastClickTime <= 2000) {
            if (lit.hasPrevious()) {
                videoactual = lit.previous();
                cargarVideo(videoactual);
            }
        } else {
            mediaPlayer.seek(mediaPlayer.getStartTime());
        }
        lastClickTime = currentTime;

        sliderVolumen.setValue(100);
    }


    private void cargarVideo(Video video) {
        mediaPlayer.stop();
        mediaPlayer.dispose();
        System.gc();
        
        videoPath = new File(video.getUrl()).toURI().toString();
        media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        titulo.setText(video.getTitulo());
        
        mediaPlayer.setOnEndOfMedia(() -> {
            if (lit.hasNext()) { 
                videoactual = lit.next();  
                cargarVideo(videoactual);
            }
        });
        
        System.out.println("Video listo, esperando 100ms para iniciar...");
        new Timeline(new KeyFrame(Duration.millis(100), e -> mediaPlayer.play())).play();
        System.out.println("Video corriendo exitosamente"); 
    }
    
    private void restart() {
        try {
            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mediaplayer.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void abrirVentanaHome() throws IOException {
        App app = new App();
        app.abrirVentanas();
    }
   
}
