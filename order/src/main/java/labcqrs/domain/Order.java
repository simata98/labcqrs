package labcqrs.domain;

import labcqrs.domain.OrderPlaced;
import labcqrs.domain.OrderCancelled;
import labcqrs.OrderApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;


@Entity
@Table(name="Order_table")
@Data

//<<< DDD / Aggregate Root
public class Order  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    private Long id;
    
    
    
    
    private String productId;
    
    
    
    
    private Integer qty;
    
    
    
    
    private String customerId;
    
    
    
    
    private Double amount;
    
    
    
    
    private String status;
    
    
    
    
    private String address;

    @PostPersist
    public void onPostPersist(){


        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();



        OrderCancelled orderCancelled = new OrderCancelled(this);
        orderCancelled.publishAfterCommit();

    
    }
    @PreRemove
    public void onPreRemove(){
    
    }

    public static OrderRepository repository(){
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(OrderRepository.class);
        return orderRepository;
    }



    public void order(){
        //implement business logic here:
        
        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();
        
        
        labcqrs.external.InventoryQuery inventoryQuery = new labcqrs.external.InventoryQuery();
        OrderApplication.applicationContext
            .getBean(labcqrs.external.Service.class)
            .( inventoryQuery);
    }
    public void cancel(){
        //implement business logic here:
        
        OrderCancelled orderCancelled = new OrderCancelled(this);
        orderCancelled.publishAfterCommit();
        
        
    }



}
//>>> DDD / Aggregate Root
