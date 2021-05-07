import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Product} from './product';
import {Cart} from './cart';

@Injectable({
  providedIn: 'root'
})
export class GenericService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  private backendUrl = 'http://localhost/StoreApp/';

  constructor(private http: HttpClient) {
  }

  fetchProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.backendUrl + `PHP/getProducts.php`)
      .pipe(catchError(this.handleError<Product[]>('fetchProducts', []))
      );
  }

  fetchProductsCategory(category: string): Observable<Product[]> {
    return this.http.get<Product[]>(this.backendUrl + `PHP/fetchProductsCategory.php?category=${category}`)
      .pipe(catchError(this.handleError<Product[]>('fetchProductsCategory', []))
      );
  }

  addToCart(name: string, category: string, price: number, cartId: number): Observable<any> {
    return this.http.post(this.backendUrl + `PHP/addCart.php`, {
      item_name: name,
      item_category: category,
      item_price: price,
      item_cartId: cartId
    });
  }

  getCartItems(): Observable<Cart[]> {
    return this.http.get<Cart[]>(this.backendUrl + `PHP/getCart.php`)
      .pipe(catchError(this.handleError<Product[]>('getCartItems', []))
      );
  }

  removeCart(itemId: number): Observable<any> {
    return this.http.post(this.backendUrl + `PHP/removeCart.php`, {id: itemId});
  }

  private handleError<T>(operation = 'operation', result?: T): (error: any) => Observable<T> {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
