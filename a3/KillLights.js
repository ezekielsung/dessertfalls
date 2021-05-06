var JavaPackages = new JavaImporter(
 Packages.ray.rage.scene.SceneManager,
 Packages.ray.rage.scene.Light,
 Packages.ray.rage.scene.Light.Type,
 Packages.ray.rage.scene.Light.Type.POINT,
 Packages.java.awt.Color
 );
// creates a RAGE object - in this case a light
with (JavaPackages)
{ 
sm.getAmbientLight().setIntensity(new Color(.05, .05, .05));
var plight = sm.getLight("testLamp3");
plight.setAmbient(new Color(.0, .0, .0));
plight.setDiffuse(new Color(.0, .0, .0));
plight.setSpecular(new Color(0.0, 0.0, 0.0));
plight.setRange(5);
print("created lights foo");
}