package hellotvxlet;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;
import org.bluray.ui.event.HRcEvent;
import org.davic.resources.ResourceClient;
import org.davic.resources.ResourceProxy;
import org.dvb.event.EventManager;
import org.dvb.event.UserEvent;
import org.dvb.event.UserEventListener;
import org.dvb.event.UserEventRepository;
import org.havi.ui.HBackgroundConfigTemplate;
import org.havi.ui.HConfigurationException;
import org.havi.ui.HPermissionDeniedException;
import org.havi.ui.HStillImageBackgroundConfiguration;
import org.havi.ui.HBackgroundDevice;
import org.havi.ui.HBackgroundImage;
import org.havi.ui.HScreen;
import org.havi.ui.event.HBackgroundImageEvent;
import org.havi.ui.event.HBackgroundImageListener;
public class HelloTVXlet implements Xlet, ResourceClient, HBackgroundImageListener, UserEventListener {
HBackgroundImage iframe[]=new HBackgroundImage[4];
HStillImageBackgroundConfiguration hsbdc;
int geladen=0;
int i=0;
int vote =0;
String programma[]={"Thuis","Familie","Komen Eten","Callboys"};
int thuis=0;
int familie=0;
int callboys=0;
int eten=0;

public void initXlet(XletContext ctx) throws XletStateChangeException {
HScreen hs=HScreen.getDefaultHScreen();
HBackgroundDevice hbgd=hs.getDefaultHBackgroundDevice();
hbgd.reserveDevice(this);
HBackgroundConfigTemplate hbgdct =new HBackgroundConfigTemplate();
hbgdct.setPreference(HBackgroundConfigTemplate.STILL_IMAGE, 
HBackgroundConfigTemplate.REQUIRED);
hsbdc=(HStillImageBackgroundConfiguration)hbgd.getBestConfiguration(hbgdct);
try {
hbgd.setBackgroundConfiguration(hsbdc);
//pizza1.zip uitpakken in
// C:\Program Files\TechnoTrend\TT-MHP-Browser\fileio\DSMCC\0.0.3
} catch (Exception ex) {
ex.printStackTrace();
} 
// bovenaan: HBackgroundImage iframe[]=new HBackgroundImage[4];
iframe[0]=new HBackgroundImage("thuis.m2v");
iframe[1]=new HBackgroundImage("familie.m2v");
iframe[2]=new HBackgroundImage("callboys.m2v");
iframe[3]=new HBackgroundImage("DIW.m2v");
iframe[0].load(this); 
iframe[1].load(this); 
iframe[2].load(this); 
iframe[3].load(this); 
// TODO: reageer op de pijltjes (zie blz 46)
}
public void imageLoaded(HBackgroundImageEvent e) {
System.out.println("M2V geladen");
geladen++;
if (geladen==4) // als alle image geladen zijn, laat het eerste dan zien
{
try {
//bovenaan bij implements HImageBackgroundListener toevoegen+import+implements...
hsbdc.displayImage(iframe[0]);
} catch (Exception ex) {
ex.printStackTrace();
}
}
}
public void pauseXlet() {
}
public void startXlet() throws XletStateChangeException {
EventManager manager = EventManager.getInstance();
UserEventRepository repository = new UserEventRepository( "Voorbeeld");
repository.addKey(org.havi.ui.event.HRcEvent.VK_LEFT);
repository.addKey(org.havi.ui.event.HRcEvent.VK_RIGHT);
repository.addKey(org.havi.ui.event.HRcEvent.VK_ENTER);
repository.addKey(org.havi.ui.event.HRcEvent.VK_COLORED_KEY_0);
manager.addUserEventListener(this,repository);
}
public void destroyXlet(boolean unconditional) throws XletStateChangeException {
}
public boolean requestRelease(ResourceProxy proxy, Object requestData) {
throw new UnsupportedOperationException("Not supported yet.");
}
public void release(ResourceProxy proxy) {
throw new UnsupportedOperationException("Not supported yet.");
}
public void notifyRelease(ResourceProxy proxy) {
throw new UnsupportedOperationException("Not supported yet.");
}
public void imageLoadFailed(HBackgroundImageEvent e) {
throw new UnsupportedOperationException("Not supported yet.");
}
public void userEventReceived(UserEvent e) {
if (e.getType() == KeyEvent.KEY_PRESSED) {
System.out.println("Pushed Button");
switch (e.getCode()) {
case HRcEvent.VK_RIGHT:
System.out.println("VK_RIGHT is PRESSED");
i++;
if (i>3) {i=0;}
try {
hsbdc.displayImage(iframe[i]);
} catch (IOException ex) {
ex.printStackTrace();
} catch (HPermissionDeniedException ex) {
ex.printStackTrace();
} catch (HConfigurationException ex) {
ex.printStackTrace();
}
break;
case HRcEvent.VK_LEFT:
System.out.println("VK_LEFT is PRESSED");
i--;
if (i<0) {i=3;}
try {
hsbdc.displayImage(iframe[i]);
} catch (IOException ex) {
ex.printStackTrace();
} catch (HPermissionDeniedException ex) {
ex.printStackTrace();
} catch (HConfigurationException ex) {
ex.printStackTrace();
}
break;
case HRcEvent.VK_ENTER:
System.out.println("VK_ENTER is PRESSED");
vote= i;
System.out.println(programma[vote] + " is uw favoriet");
if (vote==0)
{thuis++;}
if (vote==1)
{familie++;}
if (vote==2)
{callboys++;}
if (vote==3)
{eten++;}
break;
case HRcEvent.VK_COLORED_KEY_0:
System.out.println("Resultaten stemming: Thuis= "+thuis+" Familie= "+familie+" Callboys= "+callboys+" Komen eten= "+eten);
break;
}
}
}

}