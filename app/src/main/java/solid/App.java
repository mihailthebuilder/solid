/*
 * SOLID principles explanation
 */
package solid;

import java.util.List;

class S {
    /*
     * S = single-responsibility principle = A class should have only one reason to
     * change.
     * Or, in other words, a class should only have one job.
     * 
     * Let's say you have 2 classes - Square and Circle...
     */

    class Square {
        public double length;

        public Square(double length) {
            this.length = length;
        }
    }

    class Circle {
        public double radius;

        public Circle(double radius) {
            this.radius = radius;
        }
    }

    /*
     * Next, we have an AreaCalculator which calculates the area, that calculates
     * the area. But then it also outputs the area calculated as string, which
     * breaks the single-responsibility principle. If you want to add another way to
     * output, like JSON, you have to change the AreaCalculator class, which isn't
     * really what the class is supposed to do.
     */

    class AreaCalculator {
        private List<Object> shapes;

        public AreaCalculator(List<Object> shapes) {
            this.shapes = shapes;
        }

        public double getArea() {

            double sum = 0;

            for (Object shape : shapes) {
                double val = 0;

                if (shape instanceof Square) {
                    val = ((Square) shape).length * 4;
                } else if (shape instanceof Circle) {
                    val = ((Circle) shape).radius * Math.PI;
                }

                sum = sum + val;
            }
            return sum;
        }

        public String output() {
            return String.format("area is %.2f", getArea());
        }
    }

    /*
     * Instead, you should create a new class that takes in
     * an AreaCalculator and allows for multiple ways to output
     */

    class AreaCalculatorCorrect {
        private List<Object> shapes;

        public AreaCalculatorCorrect(List<Object> shapes) {
            this.shapes = shapes;
        }

        public double getArea() {

            double sum = 0;

            for (Object shape : shapes) {
                double val = 0;

                if (shape instanceof Square) {
                    val = ((Square) shape).length * 4;
                } else if (shape instanceof Circle) {
                    val = ((Circle) shape).radius * Math.PI;
                }

                sum = sum + val;
            }
            return sum;
        }
    }

    class AreaCalculatorOutputter {
        private AreaCalculatorCorrect areaCalculator;

        public AreaCalculatorOutputter(AreaCalculatorCorrect areaCalculator) {
            this.areaCalculator = areaCalculator;
        }

        public String outuputAsString() {
            return String.format("area is %.2f", this.areaCalculator.getArea());
        }

        public String outputAsJson() {
            return String.format("{area:%.2f}", this.areaCalculator.getArea());
        }
    }

}

class O {

    /*
     * O = open-closed principle = a class should be open for extension
     * and closed for modification. In other words, a class should be extendable
     * without having to modify the class itself.
     * 
     * Let's go back to our example with how AreaCalculator needs to calculate the
     * area of a list of shapes by having to check the class
     * of the object. If we have to add a triangle, we then need to modify the
     * getArea method to check for that class as well. That's clunky.
     */

    class Square {
        public double length;

        public Square(double length) {
            this.length = length;
        }
    }

    class Circle {
        public double radius;

        public Circle(double radius) {
            this.radius = radius;
        }
    }

    class AreaCalculator {
        private List<Object> shapes;

        public AreaCalculator(List<Object> shapes) {
            this.shapes = shapes;
        }

        public double getArea() {

            double sum = 0;

            for (Object shape : shapes) {
                double val = 0;

                if (shape instanceof Square) {
                    val = ((Square) shape).length * 4;
                } else if (shape instanceof Circle) {
                    val = ((Circle) shape).radius * Math.PI;
                }

                sum = sum + val;
            }
            return sum;
        }
    }

    /*
     * A better way is to add the method area to each
     * object, and have it implemented as an interface that
     * can be taken in by the AreaCalculator class.
     */

    interface ShapeWithArea {
        public double area();
    }

    class SquareCorrect implements ShapeWithArea {
        public double length;

        public SquareCorrect(double length) {
            this.length = length;
        }

        public double area() {
            return this.length * 4;
        }
    }

    class CircleCorrect implements ShapeWithArea {
        public double radius;

        public CircleCorrect(double radius) {
            this.radius = radius;
        }

        public double area() {
            return this.radius * Math.PI;
        }
    }

    class AreaCalculatorCorrect {
        private List<ShapeWithArea> shapes;

        public AreaCalculatorCorrect(List<ShapeWithArea> shapes) {
            this.shapes = shapes;
        }

        public double getArea() {

            double sum = 0;

            for (ShapeWithArea shape : shapes) {
                sum = sum + shape.area();
            }
            return sum;
        }
    }

    /*
     * Now, when you add a triangle class, you only need to implement it as a
     * ShapeWithArea interface
     * and that's it, it can be accepted in the AreaCalculator
     */
}

class L {
    /*
     * L = Liskov substitution principle. Any subclass of a class can replace the
     * class.
     * 
     * Let's say you have the AreaCalculator with the output method still there...
     */

    interface ShapeWithArea {
        public double area();
    }

    class AreaCalculator {
        private List<ShapeWithArea> shapes;

        public AreaCalculator(List<ShapeWithArea> shapes) {
            this.shapes = shapes;
        }

        public double getArea() {

            double sum = 0;

            for (ShapeWithArea shape : shapes) {
                sum = sum + shape.area();
            }
            return sum;
        }

        public String output() {
            return String.format("area is %.2f", getArea());
        }
    }

    class AreaCalculatorOutputter {
        private AreaCalculator areaCalculator;

        public AreaCalculatorOutputter(AreaCalculator areaCalculator) {
            this.areaCalculator = areaCalculator;
        }

        public String outuputAsString() {
            return String.format("area is %.2f", this.areaCalculator.getArea());
        }

        public String outputAsJson() {
            return String.format("{area:%.2f}", this.areaCalculator.getArea());
        }
    }

    /*
     * If you create a VolumeCalculator class that extends AC,
     * but replaces the output of getArea() as a list, then
     * it breaks the principle. And the consequence is that
     * you can't use it to create a AreaCalculatorOutputter
     * because the output methods require getArea as a string.
     * 
     * Instead, it has to return the correct value;
     */

    class VolumeCalculator extends AreaCalculator {

        public VolumeCalculator(List<ShapeWithArea> shapes) {
            super(shapes);
        }

        // public List<Double> getArea() {
        // ArrayList<Double> list = new ArrayList<Double>();
        // list.add(1.0);
        // list.add(2.0);
        // return list;
        // }

        public double getArea() {
            double sum = 0;

            for (ShapeWithArea shape : super.shapes) {
                sum = sum + shape.area();
            }
            return sum * sum * sum;
        }
    }
}

class I {

    /*
     * I = interface segregation principle. A client should never be forced to
     * implement an interface it doesn't use.
     * 
     * Let's say you now accept 3D objects and you want to get their volume, as well
     * as area. Simply adding volume() as a method to the ShapeWithArea interface
     * breaks this principle. ShapeWithArea should just return the area, and if you
     * add
     * the volume method it won't support the Square class.
     * 
     * You should create instead another interface for the volume method;
     */

    interface ShapeWithArea {
        public double area();

        // public double volume();
    }

    class Square implements ShapeWithArea {
        public double length;

        public Square(double length) {
            this.length = length;
        }

        public double area() {
            return this.length * 4;
        }
    }

    class Cube implements ShapeWithArea, ThreeDObjectWithVolume {
        public double area;

        public Cube(double area) {
            this.area = area;
        }

        public double area() {
            return this.area * 6;
        }

        public double volume() {
            return this.area * this.area * this.area * this.area;
        }
    }

    interface ThreeDObjectWithVolume {
        public double volume();
    }
}

class D {
    /*
     * D = dependency inversion principle. Entities must depend on abstractions, not
     * concretions. In other words, you shouldn't depend on the low-level
     * implementation, but
     * abstract it to allow decoupling.
     * 
     * For example, you using the MySqlConnection for GetCustomerData class breaks
     * this principle.
     * If you now want to enable Postgres connection, you have to modify
     * GetCustomerData.
     */
    class MySqlConnection {
        public String connect() {
            return "connection";
        }
    }

    class GetCustomerData {
        private MySqlConnection dbConnection;

        public GetCustomerData(MySqlConnection dbConnection) {
            this.dbConnection = dbConnection;
        }

        public String getData() {
            String connection = this.dbConnection.connect();
            // connection.doSomethingThatCan
            return "data";
        }
    }

    /*
     * You should instead create an interface that's used in GetCustomerData and
     * implemented by MySqlConnection
     */

    interface DbConnectionInterface {
        public String connect();
    }

    class MySqlConnectionCorrect implements DbConnectionInterface {
        public String connect() {
            return "connection";
        }
    }

    class GetCustomerDataCorrect {
        private DbConnectionInterface dbConnection;

        public GetCustomerDataCorrect(DbConnectionInterface dbConnection) {
            this.dbConnection = dbConnection;
        }

        public String getData() {
            String connection = this.dbConnection.connect();
            // connection.doSomethingThatCan
            return "data";
        }
    }
}