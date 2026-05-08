package cz.osu.swidemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // mappedBy odkazuje na název proměnné 'department' ve třídě User
    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    @JsonIgnore
    @JsonIgnoreProperties("department")
    private List<User> users = new ArrayList<>();

    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    // --- POMOCNÉ METODY PRO VAZBU 1:M ---
    // Tyto metody zajistí, že když přidáš uživatele do oddělení,
    // automaticky se u uživatele nastaví toto oddělení.

    public void addUser(User user) {
        users.add(user);
        user.setDepartment(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setDepartment(null);
    }

    // --- GETTERY A SETTERY ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // ToString upravený tak, aby nezpůsobil nekonečnou smyčku (vynecháváme seznam uživatelů)
    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}