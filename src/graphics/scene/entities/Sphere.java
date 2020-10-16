package graphics.scene.entities;

import graphics.maths.ColorRGB;
import graphics.rays.Ray;
import graphics.rays.RaycastHit;
import graphics.maths.Vector3;

public class Sphere extends SceneObject {

	// Sphere coefficients
	private final double SPHERE_KD = 0.8;
	private final double SPHERE_KS = 1.2;
	private final double SPHERE_ALPHA = 10;
	private final double SPHERE_REFLECTIVITY = 0.3;

	// The world-space position of the sphere
	private Vector3 position;

	public Vector3 getPosition() {
		return position;
	}

	// The radius of the sphere in world units
	private double radius;

	public Sphere(Vector3 position, double radius, ColorRGB colour) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = SPHERE_KD;
		this.phong_kS = SPHERE_KS;
		this.phong_alpha = SPHERE_ALPHA;
		this.reflectivity = SPHERE_REFLECTIVITY;
	}

	public Sphere(Vector3 position, double radius, ColorRGB colour, double kD, double kS, double alphaS, double reflectivity) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = kD;
		this.phong_kS = kS;
		this.phong_alpha = alphaS;
		this.reflectivity = reflectivity;
	}

	/*
	 * Calculate intersection of the sphere with the ray. If the ray starts inside the sphere,
	 * intersection with the surface is also found.
	 */
	public RaycastHit intersectionWith(Ray ray) {

		// Get ray parameters
		Vector3 O = ray.getOrigin();
		Vector3 D = ray.getDirection();
		
		// Get sphere parameters
		Vector3 C = position;
		double r = radius;

		// Calculate quadratic coefficients
		double a = D.dot(D);
		double b = 2 * D.dot(O.subtract(C));
		double c = (O.subtract(C)).dot(O.subtract(C)) - Math.pow(r, 2);
		
		// TODO: Determine if ray and sphere intersect - if not return an empty RaycastHit
		double discriminant = Math.pow(b, 2) - (4 * a * c);
		if (discriminant < 0) {
			RaycastHit empty = new RaycastHit();
			return empty;
		} else {
			// TODO: If so, work out any point of intersection
			double solution1 = ((- b) + Math.sqrt(discriminant)) / (2.0 * a); // find 2 solutions for s
			double solution2 = ((- b) - Math.sqrt(discriminant)) / (2.0 * a);

			if (solution1 < 0 && solution2 < 0){  // if both negative
				RaycastHit empty = new RaycastHit();
				return empty;
			} else {
				// TODO: Then return a RaycastHit that includes the object, ray distance, point, and normal vector

				// calculate distance from O, intersection point vect and normal to sphere at intersection point
				// for both solutions 1 and 2
				// if solution1 is +ve and smaller than solution2 (solution2 is therefore +ve), it is the closest to O
				if (solution1 > 0 && solution1 <= solution2){
					Vector3 intersection = O.add(D.scale(solution1));
					Vector3 normal = intersection.subtract(C).normalised();

					RaycastHit ray1 = new RaycastHit(this, solution1, intersection, normal);
					return ray1;

				} else {  // solution2 is the only +ve sol
					Vector3 intersection = O.add(D.scale(solution2));
					Vector3 normal = intersection.subtract(C).normalised();

					RaycastHit ray2 = new RaycastHit(this, solution2, intersection, normal);
					return ray2;
				}
			}
		}
	}

	// Get normal to surface at position
	public Vector3 getNormalAt(Vector3 position) {
		return position.subtract(this.position).normalised();
	}
}
