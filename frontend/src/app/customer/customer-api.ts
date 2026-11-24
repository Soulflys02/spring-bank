import { HttpClient, HttpContext } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Customer } from './customer';
import { Observable } from 'rxjs';
import { HANDLED_ERROR_STATUSES } from '../errors/error-interceptor';

@Service()
export class CustomerApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/customers';

  getMe(): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/me`, {
      context: new HttpContext().set(HANDLED_ERROR_STATUSES, [404]),
    });
  }

  create(): Observable<Customer> {
    return this.http.post<Customer>(this.baseUrl, null);
  }
}
