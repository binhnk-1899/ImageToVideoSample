package com.loanxu.photoeditor.utils

object Geometry {

    class Point(var x: Float, var y: Float, var z: Float) {

        fun translate(vector: Vector): Point {
            return Point(x + vector.x, y + vector.y, z + vector.z)
        }
    }

    class Bezier(var point1: Point, var point2: Point, var point3: Point, var point4: Point)

    class Eclips(var x: Float, var y: Float, var z: Float, var a: Double, var b: Double) {
        fun translate(angle: Double): Point {
            x = getCoordX(angle)
            y = (Math.tan(angle) * x).toFloat()
            return Point(x, y, z)
        }

        fun getCoordX(angle: Double): Float {
            var m = (1 / Math.pow(a, 2.0) + (Math.pow(Math.tan(angle), 2.0) / Math.pow(b, 2.0)))
            var x = Math.sqrt(1 / m).toFloat()
            return x
        }
    }

    class FireFlySize() {
        //y = sin(x)
        // x = (0,360)
        fun translateSin(point: Point): Point {
            var p = point
            p.x += Math.PI.toFloat() / 60
            p.y = Math.sin(p.x.toDouble()).toFloat()
            return p
        }

        fun translate(point: Point): Point {
            var p = point
            p.x += 0.01f
            p.y -= 0.01f
            p.z -= 0.01f
            return p
        }
    }

    class Circle(val center: Point, val radius: Float) {

        fun scale(scale: Float): Circle {
            return Circle(center, radius * scale)
        }
    }

    class Cylinder(val center: Point, val radius: Float, val height: Float)

    class Ray(val point: Point, val vector: Vector)

    class Vector(var x: Float, var y: Float, var z: Float) {

        fun length(): Float {
            return Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        }

        fun crossProduce(other: Vector): Vector {
            return Vector(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
            )
        }

        fun dotProduct(other: Vector): Float {
            return (x * other.x
                    + y * other.y
                    + z * other.z)
        }


        fun scale(f: Float): Vector {
            
            return Vector(x * f, y * f, z * f)
        }
    }

    class Sphere(val center: Point, val radius: Float)

    fun vectorBetween(from: Point, to: Point): Vector {
        
        return Vector(to.x - from.x, to.y - from.y, to.z - from.z)
    }

    fun intersects(sphere: Sphere, ray: Ray): Boolean {
        
        return distanceBetween(sphere.center, ray) < sphere.radius
    }

    private fun distanceBetween(point: Point, ray: Ray): Float {
        val p1ToPoint = vectorBetween(ray.point, point)
        val p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point)
        val areaOfTriangleTimesTwo = p1ToPoint.crossProduce(p2ToPoint).length()
        val lengthOfBase = ray.vector.length()
        return areaOfTriangleTimesTwo / lengthOfBase
    }

    class Plane(val point: Point, val normal: Vector)

    fun intersectionPoint(ray: Ray, plane: Plane): Point {
        val rayToPlaneVector = vectorBetween(ray.point, plane.point)
        val scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal)
        return ray.point.translate(ray.vector.scale(scaleFactor))
    }
}
