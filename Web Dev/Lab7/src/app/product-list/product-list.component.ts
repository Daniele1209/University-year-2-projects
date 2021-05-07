import { Component, OnInit } from '@angular/core';
import {Product} from '../product';
import {GenericService} from '../generic.service';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  // tslint:disable-next-line:ban-types
  totalRecords: Number;
  // tslint:disable-next-line:ban-types
  page: Number = 1;
  // tslint:disable-next-line:ban-types
  itemIndex: Number = 1;

  constructor(private genericService: GenericService, private router: Router) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.genericService.fetchProducts().subscribe(products => {
      this.products = products;
      this.totalRecords = this.products.length;
    }
    );

  }

  navigateToCart(name: string, category: string, price: number): void {
    this.genericService.addToCart(name, category, Number(price), 1).subscribe(() => {
      });
  }

  filterByCategory(category: string): void {
    this.products = [];
    this.genericService.fetchProductsCategory(category).subscribe(products => {
        this.products = products;
        this.totalRecords = this.products.length;
      }
    );
  }

}
