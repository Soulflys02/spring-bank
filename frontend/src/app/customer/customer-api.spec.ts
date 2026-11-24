import { TestBed } from '@angular/core/testing';
import { CustomerApi } from './customer-api';
import { Customer } from './customer';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { firstValueFrom } from 'rxjs';
import { HANDLED_ERROR_STATUSES } from '../errors/error-interceptor';

describe('CustomerApi', () => {
  let service: CustomerApi;
  let httpTesting: HttpTestingController;

  const customer: Customer = {
    firstName: 'Ada',
    lastName: 'Lovelace',
    email: 'ada.lovelace@example.com',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    httpTesting = TestBed.inject(HttpTestingController);
    service = TestBed.inject(CustomerApi);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should fetch the current customer', async () => {
    const me$ = service.getMe();
    const mePromise = firstValueFrom(me$);

    const req = httpTesting.expectOne({ method: 'GET', url: '/api/v1/customers/me' });
    req.flush(customer);

    expect(req.request.context.get(HANDLED_ERROR_STATUSES)).toEqual([404]);
    expect(await mePromise).toEqual(customer);
  });

  it('should create the current customer', async () => {
    const create$ = service.create();
    const createPromise = firstValueFrom(create$);

    const req = httpTesting.expectOne({ method: 'POST', url: '/api/v1/customers' });
    req.flush(customer);

    expect(req.request.body).toBeNull();
    expect(await createPromise).toEqual(customer);
  });
});
