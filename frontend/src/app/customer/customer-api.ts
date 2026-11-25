import { HttpClient, HttpContext } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerResponse } from '../api/model/customer-response';
import { HANDLED_ERROR_STATUSES } from '../errors/error-interceptor';

@Service()
export class CustomerApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/customers';

  getMe(): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(`${this.baseUrl}/me`, {
      context: new HttpContext().set(HANDLED_ERROR_STATUSES, [404]),
    });
  }

  create(): Observable<CustomerResponse> {
    return this.http.post<CustomerResponse>(this.baseUrl, null);
  }
}
