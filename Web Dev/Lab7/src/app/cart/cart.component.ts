import { Component, OnInit } from '@angular/core';
import {Cart} from '../cart';
import {GenericService} from '../generic.service';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  products: Cart[] = [];

  constructor(private genericService: GenericService, private router: Router) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.genericService.getCartItems().subscribe(products => {
        this.products = products;
      }
    );

  }

  deleteCart(id: number, price: number): void {
    this.genericService.removeCart(Number(id)).subscribe(() => {
    });
    this.products = [];
    this.getProducts();

  }

}
