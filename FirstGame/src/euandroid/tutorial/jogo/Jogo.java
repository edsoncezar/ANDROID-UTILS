package euandroid.tutorial.jogo;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class Jogo extends BaseGameActivity implements IAccelerometerListener {

	private PhysicsWorld w;
	private int camera_width;
	private int camera_height;
	private Camera c;
	private Rectangle ball;

	@Override
	public Engine onLoadEngine() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		camera_width = dm.widthPixels;
		camera_height = dm.heightPixels;
		c = new Camera(0, 0, camera_width, camera_height);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(camera_width, camera_height), c));
	}

	@Override
	public void onLoadResources() {
		this.enableAccelerometerSensor(this);

	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		Scene s = new Scene(2);//cria uma cena, o int � a quantidade de layers desta cena
		
		//Criando objetos visuais retangulos
		Rectangle bottom = new Rectangle(0, camera_height - 1,
				camera_width, 1);
		Rectangle top = new Rectangle(0, 0, camera_width, 1);
		Rectangle left = new Rectangle(0, 0, 1, camera_height);
		Rectangle right = new Rectangle(camera_width - 1, 0, 1,
				camera_height);

		//adicionado na tela. � preciso adicionar em um dos layers, neste caso o layer no layer mais baixo, bottom
		s.getBottomLayer().addEntity(bottom);
		s.getBottomLayer().addEntity(right);
		s.getBottomLayer().addEntity(top);
		s.getBottomLayer().addEntity(left);

		/*Cria um PhysicsWorld. Este � o Respons�vel por controlar toda a f�sica do seu jogo.
		 * � este objeto que vai tratar a l�gica da gravidade, velocidade, elasticidade, etc
		*/

		w = new PhysicsWorld(new Vector2(0, 0), false);
		
		/*FixtureDef �  a classe respons�vel pro definir as propriedades f�sicas do objeto
		 * no mundo f�sico. Possui 3 parametros: Densidade, elasticidade e fric��o. s�o valores float, onde 
		 * o mais comum � ir de 0 at� 1, por�m pode ser adicionado qualquer float.
		 */

		FixtureDef f = PhysicsFactory.createFixtureDef(0.7f, 0.3f, 1);

		/*Body � a classe respons�vel por representar o objeto gr�fico no Physcs World.
		 * Os seus parametros s�o: o PhysicsWorld a que pertecem, qual � o objeto gr�fico que ele pertence,
		 *  qual o tipo de Body(pode ser dynamic(din�mico):responde as intera��es de colis�o mudando a velocidade,
		 *  arota��o e a posi��o;
		 *  static: N�o reage as intera��es de colis��o nem a gravidade. Normalmente s�o obst�culos est�ticos;
		 *  KinematicBody: N�o vejo diferen�a entre ele e o static.)
		 *  
		 *   e o ultimo parametro � o FixtureDef do corpo, ou seja a sua composi��o f�sica.
		 */
		Body topb = PhysicsFactory.createBoxBody(w, top,
				BodyType.KinematicBody, f);
		Body backb = PhysicsFactory.createBoxBody(w, bottom,
				BodyType.KinematicBody, f);
		Body leftb = PhysicsFactory.createBoxBody(w, right,
				BodyType.KinematicBody, f);
		Body rightb = PhysicsFactory.createBoxBody(w, left,
				BodyType.KinematicBody, f);

		/*Registra uma conex�o entre o Body e a sua imagem, para garantir que o que est� sendo mostrado na tela
		 * corresponde ao que est� acontecendo no PhysicsWorld
		 */
		
		w.registerPhysicsConnector(new PhysicsConnector(top, topb));
		w.registerPhysicsConnector(new PhysicsConnector(bottom, backb));
		w.registerPhysicsConnector(new PhysicsConnector(right, rightb));
		w.registerPhysicsConnector(new PhysicsConnector(left, leftb));

		ball = new Rectangle(0, 0, camera_width * 0.05f, camera_width * 0.05f);
		ball.setPosition((camera_width - ball.getWidth()) / 2,
				(camera_height - ball.getHeight()) / 2);
		ball.setColor(1, 1, 1);
		FixtureDef ballf = PhysicsFactory.createFixtureDef(0.8f, 0.2f, 0);
		Body ballB=PhysicsFactory.createBoxBody(w, ball, BodyType.DynamicBody, ballf);
		w.registerPhysicsConnector(new PhysicsConnector(ball, ballB, true,
				true, true, true));

		s.getBottomLayer().addEntity(ball);

		//registra o PhysicsWorld na cena, para que sua l�gica seja executada.
		s.registerUpdateHandler(w);

		return s;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
		w.setGravity(new Vector2(-pAccelerometerData.getX(),pAccelerometerData.getY()));

	}

}