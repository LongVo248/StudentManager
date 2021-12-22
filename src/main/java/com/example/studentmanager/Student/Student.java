package com.example.studentmanager.Student;


import com.example.studentmanager.Audit.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "student")
//@Data
@EntityListeners(AuditingEntityListener.class)
public class Student extends Auditable<String> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Thiếu Username")
    private String name;

    @Column(name = "email")
    @Email(message = "Email không hợp lệ!")
    @NotBlank(message = "Thiếu Email")
    private String email;

    @Column(name = "address")
    @NotEmpty(message = "Thiếu Address")
    private String address;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please provide a date.")
    private Date dob;

    @Min(value = 18, message = "Under 18 years old")
    @Max(value = 80, message = "Over 80 years old")
    public int getAge() {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        birthDate.setTime(dob);
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);


        if ((birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
                (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;


        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) &&
                (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
    }

    @Column(name = "phone")
    @Pattern(regexp = "(((^(\\+84|84|0|0084){1})([3|5|7|8|9]))+([0-9]{8})$)", message = "Phone numbers is not valid")
    private String phone;

    public Student() {
    }

    public Student(String name, String email, String address, Date dob, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.dob = dob;
        this.phone = phone;
    }

    public Student(int id, String name, String email, String address, Date dob, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.dob = dob;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", dob=" + dob +
                ", phone='" + phone + '\'' +
                '}';
    }
}
